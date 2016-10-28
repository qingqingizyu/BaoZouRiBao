package com.team.android_baozou.main.view;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.team.android_baozou.R;
import com.team.android_baozou.Thread.JsonThread;
import com.team.android_baozou.bean.HomeListBean;
import com.team.android_baozou.bean.PagerBean;
import com.team.android_baozou.main.adapter.HomeAdapter;
import com.team.android_baozou.main.adapter.MyAdapter;
import com.team.android_baozou.utils.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * 首页的布局
 */
public class HomeFragment extends Fragment implements JsonThread.OnGetJsonResultLinstener, OnRefreshListener,AdapterView.OnItemClickListener {

    private static final int HAND_ONE = 1;
    private String HOMEURL = "http://dailyapi.ibaozou.com/api/v30/documents/latest";
    private ViewPager VpHome;
    private ListView lvHome;
    private TextView tvTitle;
    private LinearLayout llPoints;
    private String HomeTime;
    private String HomePath;
    private String path;
    private List<HomeListBean> mList = new ArrayList<>();
    /**
     * 自动轮播消息处理
     */
    //处理消息
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int index = msg.arg1;
                VpHome.setCurrentItem(index);
            } else if (msg.what == 2) {
                loadJson(path);
                homeSwipe.setRefreshing(false);
            }
        }
    };


    private SwipeRefreshLayout homeSwipe;
    private RefreshListView homeList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LoadData();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        //初始化Fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        LoadData();
    }

    /**
     * 初始化
     *
     * @param view
     */
    protected void initView(View view) {
        VpHome = (ViewPager) view.findViewById(R.id.vp_home);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        llPoints = (LinearLayout) view.findViewById(R.id.ll_points);
        /*List初始化*/
        homeSwipe = (SwipeRefreshLayout) view.findViewById(R.id.Home_swipe);
        homeList = (RefreshListView) view.findViewById(R.id.Home_list);
        initHomeListView();
    }

    /**
     * 方法名称：ViewPager
     * 备注：首页幻灯片
     */
    /*线程控制器*/
    private void LoadData() {
        new JsonThread(this, getActivity()).execute(HOMEURL);

    }

    /*线程消息回调，以及幻灯片工作处理*/
    @Override
    public void OnGetJsonResult(List<PagerBean> result) {
              /*得到标题和图片数据*/
        final String[] title = new String[result.size()];
        List<Bitmap> image = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            PagerBean bean = result.get(i);
            String Ititle = bean.getTitle();
            Bitmap imagesI = bean.getImage();
            title[i] = Ititle;
            image.add(imagesI);
        }

        final List<ImageView> images = new ArrayList<>();
        //图片填充布局
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < image.size(); i++) {
            ImageView img = new ImageView(getActivity());
            img.setImageBitmap(image.get(i));
            img.setMaxHeight(lParams.height); //设置图片的高度
            img.setMaxWidth(lParams.width); //设置图片的宽度
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            images.add(img);

            //添加小圆点
            View point = new View(getActivity());
            //获取屏幕信息的类
            DisplayMetrics metrics = new DisplayMetrics();
            //指定数值的单位
            float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, metrics);
            float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, metrics);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) width, (int) height);
            //设置左边距
            params.leftMargin = 5;
            point.setLayoutParams(params);
            point.setBackgroundResource(R.mipmap.dot_normal);
            assert llPoints != null;
            llPoints.addView(point);
        }

        //设置标题和小圆点的默认值
        assert tvTitle != null;
        tvTitle.setText(title[0]);
        assert llPoints != null;
        llPoints.getChildAt(0).setBackgroundResource(R.mipmap.dot_enable);

        assert VpHome != null;
        VpHome.setAdapter(new MyAdapter(images));


        //解决初始情况下,不能往左滑动的小bug.
        //VpHome.setCurrentItem(0);
        VpHome.setCurrentItem(10000);
        //ViewPager滚动动改变监听
        VpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //改变标题
                tvTitle.setText(title[position % images.size()]);
                //第二种写法:
                for (int i = 0; i < llPoints.getChildCount(); i++) {
                    llPoints.getChildAt(i).setBackgroundResource(R.mipmap.dot_normal);
                    if (i == position % images.size()) {
                        llPoints.getChildAt(position % images.size()).setBackgroundResource(R.mipmap.dot_enable);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //开启线程,自动轮播线程
        final AutoRunThread mTask = new AutoRunThread();
        mTask.startRun();

        //处理ViewPager的触摸事件
        VpHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下事件,停止自动轮播
                        mTask.stopRun();
                        break;
                    case MotionEvent.ACTION_CANCEL://取消和抬起事件,开始自动轮播
                    case MotionEvent.ACTION_UP:
                        mTask.startRun();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(getActivity(),VideoWeb.class);
        HomeListBean ListBean = mList.get(position-1);
        String url = ListBean.getUrl();
        intent.putExtra("web", url);
        intent.putExtra("logo",ListBean.getThumbnail());
        intent.putExtra("title",ListBean.getTitle());
        intent.putExtra("autorName",ListBean.getAuthor_name());
        intent.putExtra("authorImage",ListBean.getAuthor_avatar());
        intent.putExtra("sourceName",ListBean.getSource_name());

        startActivity(intent);
    }




    /**
     * 线程
     * 广告自动轮播处理事务
     */
    //实现广告轮播的自动切换效果
    class AutoRunThread implements Runnable {
        //轮播时间
        private long delayTime = 3000;
        //任务是否开始
        private boolean isStart = false;

        //开始任务
        public void startRun() {
            if (!isStart) {
                //清除本次任务之前的所有其他任务
                mHandler.removeCallbacks(this);
                isStart = true;
                mHandler.postDelayed(this, delayTime);
            }
        }

        //停止任务
        public void stopRun() {
            if (isStart) {
                isStart = false;
                mHandler.removeCallbacks(this);
            }
        }

        @Override
        public void run() {

            //获取当前页面的索引值
            int currentItem = VpHome.getCurrentItem();
            Message msg = Message.obtain();
            msg.what = 1;
            msg.arg1 = ++currentItem;
            mHandler.sendMessage(msg);

            //mHandler.sendMessageDelayed();
            //递归...
            mHandler.postDelayed(this, delayTime);
        }
    }

    /***
     * 方法名称ListView
     * 备注:首页列表事务
     */

    private void initHomeListView() {
        Date dt = new Date();
        Long time = dt.getTime();
        String s = time.toString();
        String substring = s.substring(0, 10);
        path = "http://dailyapi.ibaozou.com/api/v30/documents/latest";
        HomePath = path + "?timestamp=" + substring + "&";
        loadJson(path);
        assert homeSwipe != null;
        homeSwipe.setColorSchemeResources(R.color.colorAccent);
        homeSwipe.setProgressBackgroundColorSchemeColor(Color.WHITE);
        homeSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {//耗时的网络访问--->加载新数据
                    @Override
                    public void run() {
                        SystemClock.sleep(3000);
                        mHandler.sendEmptyMessage(2);
                    }
                }.start();
            }
        });

        homeList.setOnRefreshListener(this);
        homeList.setOnItemClickListener(this);
    }

    /*耗时的操作，下载JSon*/
    public void loadJson(String s) {
        OkHttpUtils.getDataAsync(String.format(s, 1), new Callback() {
            private String time;
            private List<HomeListBean> list = new ArrayList<>();

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject obj = new JSONObject(json);
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

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mList.addAll(list);
                        HomeAdapter adapter = new HomeAdapter();
                        adapter.setData(mList);
                        homeList.setAdapter(adapter);
                        HomeTime = time;

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
                homeList.hideHeaderView();
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(5000);
                HomePath = path + "?timestamp=" + HomeTime + "&";
                loadJson(HomePath);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // 控制脚布局隐藏
                homeList.hideFooterView();
            }
        }.execute(new Void[]{});
    }


}
