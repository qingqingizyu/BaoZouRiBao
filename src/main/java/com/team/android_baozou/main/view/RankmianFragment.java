package com.team.android_baozou.main.view;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.team.android_baozou.R;
import com.team.android_baozou.base.UserBean;
import com.team.android_baozou.drawer.view.RankActivity;
import com.team.android_baozou.drawer.view.RankFragment;
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
public class RankmianFragment extends Fragment {

    @BindView(R.id.rank_list)
    ListView rankList;
    private List<UserBean> mList = new ArrayList<>();
    private String mTime;

    public RankmianFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rankmian, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Date dt = new Date();
        Long time = dt.getTime();
        String s = time.toString();
        String substring = s.substring(0, 10);
        String path = (new RankFragment()).RankPath();
//        Log.d("RankmianFragment", path);
        if (path==null){
            path = "http://dailyapi.ibaozou.com/api/v31/rank/read/day";
            loadJson(path);
        }
        if (path == "http://dailyapi.ibaozou.com/api/v31/rank/read/day") {
            loadJson(path);
        }
        if (path == "http://dailyapi.ibaozou.com/api/v31/rank/read/week") {
            loadJson(path);
        }
        if (path == "http://dailyapi.ibaozou.com/api/v31/rank/read/month") {
            loadJson(path);
        }

        rankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(getActivity(), RankActivity.class);
                intent.putExtra("URL", "http://baozouribao.com/documents/"+mList.get(position).getDocument_id());
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
                            UserBean item = gson.fromJson(String.valueOf(jsonObject), UserBean.class);
                            list.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mList.addAll(list);
                        UserAdapter adapter = new UserAdapter();
                        adapter.setData(mList);
                        rankList.setAdapter(adapter);
                        mTime = time;
                    }
                });

            }
        }, getActivity());

    }

    private Handler handler = new Handler();


}
