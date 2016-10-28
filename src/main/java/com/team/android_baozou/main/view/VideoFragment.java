package com.team.android_baozou.main.view;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.team.android_baozou.R;
import com.team.android_baozou.base.BaseFragment;
import com.team.android_baozou.base.VIdeoBean;
import com.team.android_baozou.main.adapter.VideoAdapter;
import com.team.android_baozou.utils.OkHttpUtils;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

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
public class VideoFragment extends BaseFragment implements OnRefreshListener {


    @BindView(R.id.vi_list)
    RefreshListView viList;
    //    @BindView(R.id.video_swipe)
//    SwipeRefreshLayout videoSwipe;
    private List<VIdeoBean> mList = new ArrayList<>();
    private String mTime;
    private String mPath;
    //    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 1) {
//                loadJson(path);
//                videoSwipe.setRefreshing(false);
//            }
//        }
//    };
    private String path;

    @Override
    protected void initView() {

    }

    @Override
    public int getlayoutId() {
        return R.layout.fragment_video;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        ButterKnife.bind(this, rootView);
//        return rootView;
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Date dt = new Date();
        Long time = dt.getTime();
        String s = time.toString();
        String substring = s.substring(0, 10);
        path = "http://dailyapi.ibaozou.com/api/v30/documents/videos/latest";
        mPath = path + "?timestamp=" + substring + "&";
        loadJson(path);

        viList.setOnRefreshListener(this);
        viList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), VideoWeb.class);
                String url = mList.get(position - 1).getShare_url();
                String title = mList.get(position - 1).getTitle();
                intent.putExtra("web", url);
                intent.putExtra("logo",mList.get(position-1).getImage());
                intent.putExtra("title",title);
                intent.putExtra("autorName","");
                intent.putExtra("authorImage","");
                intent.putExtra("sourceName","");
                startActivity(intent);


            }
        });

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

            Toast.makeText(getActivity(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

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
                viList.hideHeaderView();
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
                viList.hideFooterView();
            }
        }.execute(new Void[]{});

    }

    public void loadJson(String s) {
        OkHttpUtils.getDataAsync(String.format(s, 1), new Callback() {
            private String time;
            private List<VIdeoBean> list = new ArrayList<>();

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject object = new JSONObject(json);
                        JSONArray array = object.getJSONArray("data");
                        time = object.getString("timestamp");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            Gson gson = new Gson();
                            VIdeoBean item = gson.fromJson(String.valueOf(jsonObject), VIdeoBean.class);
                            list.add(item);
                            String url = list.get(0).getFile_url();
                            Log.d("VideoFragment", url);
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

                        VideoAdapter adapter = new VideoAdapter();
                        adapter.setData(mList, getActivity());
                        viList.setAdapter(adapter);
                        mTime = time;

                    }
                });
            }
        }, getActivity());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
