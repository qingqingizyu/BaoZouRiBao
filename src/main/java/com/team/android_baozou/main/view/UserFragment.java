package com.team.android_baozou.main.view;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.team.android_baozou.R;
import com.team.android_baozou.base.BaseFragment;
import com.team.android_baozou.base.UserBean;
import com.team.android_baozou.main.adapter.UserAdapter;
import com.team.android_baozou.utils.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends BaseFragment implements OnRefreshListener {


//    @BindView(R.id._swipe)
//    SwipeRefreshLayout Swipe;
    @BindView(R.id.user_list)
    RefreshListView userList;

    private List<UserBean> mList = new ArrayList<>();
    private String mTime;
    private String mPath;
//        private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 1) {
//                loadJson(path);
//                Swipe.setRefreshing(false);
//            }
//        }
//    };
    private String path;

    @Override
    protected void initView() {

    }

    @Override
    public int getlayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Date dt = new Date();
        Long time = dt.getTime();
        String s = time.toString();
        String substring = s.substring(0, 10);
        path = "http://dailyapi.ibaozou.com/api/v31/documents/contributes/latest";
        mPath = path + "?timestamp=" + substring + "&";
        loadJson(path);

//        assert Swipe != null;
//        Swipe.setColorSchemeResources(R.color.colorAccent);
//        Swipe.setProgressBackgroundColorSchemeColor(Color.WHITE);
//        Swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Thread() {//耗时的网络访问--->加载新数据
//                    @Override
//                    public void run() {
//                        SystemClock.sleep(3000);
//                        mHandler.sendEmptyMessage(1);
//                    }
//                }.start();
//            }
//        });

        userList.setOnRefreshListener(this);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),UserVideo.class);
                int Webid = mList.get(position-1).getDocument_id();
                intent.putExtra("userweb",String.valueOf(Webid));
                startActivity(intent);

            }
        });


    }

    public void loadJson(String s) {
        OkHttpUtils.getDataAsync(String.format(s, 1), new Callback() {
            private String time;
            private List<UserBean> list = new ArrayList<>();

            @Override
            public void onFailure(Request request, IOException e) {

            }
            @Override
            public void onResponse(Response response)  {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject object = new JSONObject(json);
                        JSONArray array = object.getJSONArray("data");
                        time = object.getString("timestamp");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            Gson gson = new Gson();
                            UserBean item = gson.fromJson(String.valueOf(jsonObject), UserBean.class);
                            list.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mList.addAll(list);
                        UserAdapter adapter = new UserAdapter();
                        adapter.setData(mList);
                        userList.setAdapter(adapter);
                        mTime = time;

                    }
                });
            }
        }, getActivity());

    }

    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(2000);

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                loadJson(path);
                userList.hideHeaderView();
            }
        }.execute(new Void[]{});

    }

    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(5000);
                mPath = path + "?timestamp=" + mTime + "&";
                loadJson(mPath);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // 控制脚布局隐藏
                userList.hideFooterView();
            }
        }.execute(new Void[]{});
    }
}

