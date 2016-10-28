package com.team.android_baozou.drawer.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.team.android_baozou.R;
import com.team.android_baozou.base.BaseFragment;
import com.team.android_baozou.bean.ChannelBean;
import com.team.android_baozou.drawer.adapter.ChannelAdapter;
import com.team.android_baozou.main.view.MainActivity;
import com.team.android_baozou.utils.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 * 频道的Fragment
 */
public class ChannelFragment extends BaseFragment implements AdapterView.OnItemClickListener{


    private ListView mVp;
    private String path="http://dailyapi.ibaozou.com/api/v31/channels/index?page=1&per_page=10&";
    private Toolbar channelToolBar;
    List<ChannelBean> mList=new ArrayList<>();
    //初始化view
    @Override
    protected void initView() {

    }


    //加载Fragment的布局
    @Override
    public int getlayoutId() {
        return R.layout.fragment_channel;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewChannel(view);
        loadJson(path);
    }



    private void initViewChannel(View view) {
        mVp = (ListView) view.findViewById(R.id.channel_lv);
        channelToolBar = (Toolbar) view.findViewById(R.id.channel_toolbar);
        mVp.setOnItemClickListener(this);
        initTooBar();
    }
    //toolBar设置
    private void initTooBar() {
        channelToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).Draweropen();
            }
        });
        }

    public void loadJson(String path){
        OkHttpUtils.getDataAsync(path, new Callback() {
            List<ChannelBean> list=new ArrayList<>();

            @Override
            public void onFailure(Request request, IOException e) {
//                Toast.makeText(getActivity(), "频道栏目下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    /*开始解析*/
                    try {

                        String json = response.body().string();
                        JSONObject obj=new JSONObject(json);
                        JSONArray data = obj.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String name = object.getString("name");
                            String summary = object.getString("summary");
                            String thumbnail = object.getString("thumbnail");
                            ChannelBean bean=new ChannelBean(name,summary,thumbnail);
                            list.add(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mList.addAll(list);
                        ChannelAdapter adapter=new ChannelAdapter();
                        adapter.setData(list);
                        mVp.setAdapter(adapter);
                    }
                });
            }
        },getActivity());

    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String[] datas = getUrl();
        String data = datas[position];
        ChannelBean bean = mList.get(position);
        String name = bean.getName();
        Intent intent=new Intent(getActivity(),ChannelListActivity.class);
        intent.putExtra("HttpUrl",data);
        intent.putExtra("name",name);
        startActivity(intent);
    }

    /*数据源*/
    public String[] getUrl(){
        String[] listUrl=new String[]{"http://dailyapi.ibaozou.com/api/v30/channels/3","http://dailyapi.ibaozou.com/api/v30/channels/4","http://dailyapi.ibaozou.com/api/v30/channels/5"
        ,"http://dailyapi.ibaozou.com/api/v30/channels/6","http://dailyapi.ibaozou.com/api/v30/channels/7","http://dailyapi.ibaozou.com/api/v30/channels/8",
        "http://dailyapi.ibaozou.com/api/v30/channels/9","http://dailyapi.ibaozou.com/api/v30/channels/10","http://dailyapi.ibaozou.com/api/v30/channels/11","http://dailyapi.ibaozou.com/api/v30/channels/12","http://dailyapi.ibaozou.com/api/v30/channels/14",
        "http://dailyapi.ibaozou.com/api/v30/channels/59","http://dailyapi.ibaozou.com/api/v30/channels/72"};
        return listUrl;
    }

}
