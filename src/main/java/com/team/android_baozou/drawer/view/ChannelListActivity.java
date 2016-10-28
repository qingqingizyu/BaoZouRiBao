package com.team.android_baozou.drawer.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.team.android_baozou.R;
import com.team.android_baozou.bean.HomeListBean;
import com.team.android_baozou.main.adapter.HomeAdapter;
import com.team.android_baozou.main.view.OnRefreshListener;
import com.team.android_baozou.main.view.RefreshListView;
import com.team.android_baozou.main.view.VideoWeb;
import com.team.android_baozou.utils.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChannelListActivity extends Activity implements OnRefreshListener,AdapterView.OnItemClickListener {
    private SwipeRefreshLayout channel_swipe;
    private RefreshListView channel_lv;
    private String path;
    private String mPath;
    private String HomeTime;
    private List<HomeListBean> mList = new ArrayList<>();

    //处理消息
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                loadJson(path);
                channel_swipe.setRefreshing(false);
            }
        }
    };
    private Toolbar channelactivityBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);
        initView();
        initCode();
    }


    /*初始化*/
    private void initView() {
        channel_swipe = (SwipeRefreshLayout) findViewById(R.id.Channel_a_swipe);
        channel_lv = (RefreshListView) findViewById(R.id.Channel_a_list);
        channelactivityBar = (Toolbar) findViewById(R.id.activity_channel_toolbat);
    }
    /*代码开始*/
    private void initCode() {
        Date dt = new Date();
        Long time = dt.getTime();
        String s = time.toString();
        String substring = s.substring(0, 10);
        Intent intent = getIntent();
        String httpUrl = intent.getStringExtra("HttpUrl");
        String name = intent.getStringExtra("name");
        channelactivityBar.setTitle(name);
        path = httpUrl;
        mPath = path + "?timestamp=" + substring + "&";
        loadJson(path);
        assert channel_swipe != null;
        channel_swipe.setColorSchemeResources(R.color.colorAccent);
        channel_swipe.setProgressBackgroundColorSchemeColor(Color.WHITE);
        channel_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {//耗时的网络访问--->加载新数据
                    @Override
                    public void run() {
//                        SystemClock.sleep(3000);
                        mHandler.sendEmptyMessage(2);
                    }
                }.start();
            }
        });

        channel_lv.setOnRefreshListener(this);
        channel_lv.setOnItemClickListener(this);
        channelactivityBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private void loadJson(String path) {
        OkHttpUtils.getDataAsync(String.format(path, 1), new Callback() {
            private String time;
            private List<HomeListBean> list=new ArrayList<>();
            @Override
            public void onFailure(Request request, IOException e) {
//                Toast.makeText(ChannelListActivity.this, "更新失败，请检查网络！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String string = response.body().string();
                        JSONObject obj=new JSONObject(string);
                        JSONArray data = obj.getJSONArray("data");
                        time = obj.getString("timestamp");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String title = object.getString("title");
                            String thumbnail = object.getString("thumbnail");
                            String author_name = object.getString("author_name");
                            String author_avatar = object.getString("author_avatar");
                            String url;
                            String display_type = object.getString("display_type");
                            String source_name;
                            /*判断解析*/
                            if (display_type.equals("1")) {
                                 url = object.getString("share_url");
                                source_name = object.getString("section_name");
                            }else {
                                 url = object.getString("url");
                                source_name = object.getString("source_name");
                            }
                            HomeListBean Bean = new HomeListBean(title, thumbnail, author_name, author_avatar, source_name,url);
                            list.add(Bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mList.addAll(list);
                        HomeAdapter adapter = new HomeAdapter();
                        adapter.setData(mList);
                        channel_lv.setAdapter(adapter);
                        HomeTime = time;
                    }
                });
            }
        },this);

    }

    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                loadJson(path);
                channel_lv.hideHeaderView();
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);
                mPath = path + "?timestamp=" + HomeTime + "&";
                loadJson(mPath);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // 控制脚布局隐藏
                channel_lv.hideFooterView();
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent=new Intent(ChannelListActivity.this,VideoWeb.class);
        HomeListBean ListBean = mList.get(position);
        String url = ListBean.getUrl();
        intent.putExtra("web", url);
        intent.putExtra("logo",ListBean.getThumbnail());
        intent.putExtra("title",ListBean.getTitle());
        intent.putExtra("autorName",ListBean.getAuthor_name());
        intent.putExtra("authorImage",ListBean.getAuthor_avatar());
        intent.putExtra("sourceName",ListBean.getSource_name());
        startActivity(intent);
    }

}
