package com.team.android_baozou.main.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.team.android_baozou.R;
import com.team.android_baozou.base.BaseActivity;
import com.team.android_baozou.db.DBHelper;
import com.team.android_baozou.drawer.view.ChannelFragment;
import com.team.android_baozou.drawer.view.CollectActivity;
import com.team.android_baozou.drawer.view.LoginActivity;
import com.team.android_baozou.drawer.view.RankFragment;
import com.team.android_baozou.drawer.view.SettingActivity;
import com.team.android_baozou.main.adapter.MainAdapter;
import com.team.android_baozou.utils.FragmentFactroy;
import com.team.android_baozou.utils.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    //toolbar绑定
    @BindView(R.id.main_bar)
    Toolbar mMainBar;
    //tab绑定
    @BindView(R.id.main_tab)
    TabLayout mMainTab;
    //viewpager绑定
    @BindView(R.id.main_pager)
    ViewPager mMainPager;
    //抽屉里布局的绑定
    @BindView(R.id.navigation_drawer)
    NavigationView mNavigationDrawer;
    //抽屉的绑定
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    //帧布局的绑定
    @BindView(R.id.drawer_frame)
    FrameLayout mFrameLayout;
    //包裹toolbar和viewpager的线性布局
    @BindView(R.id.drawer_linear)
    LinearLayout mDrawerLinear;


    private int count = 0;
    private SQLiteDatabase mDb;
    private TextView mDrawerName;
    private ImageView mDrawerIcon;

    //创建一个接口回掉抽屉的布局
    public interface OnOpenListener {
        void openListener(DrawerLayout drawer);
    }

    @Override
    protected void initView() {
        //初始化设置
        initSetting();
        //设置viewpager和tab的联动
        setPagerWithTab();
        //获取数据库对象
        DBHelper dbHelper = new DBHelper(this);
        mDb = dbHelper.getWritableDatabase();
        //设置抽屉里面的内容
        setDrawble();
        //设置toolbar
        setToolBar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        String name = null;
        String url = null;
        Cursor cursor = mDb.rawQuery("select*from user", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex("name"));
                url = cursor.getString(cursor.getColumnIndex("url"));
            }
            //记得关流，防止OOM
            cursor.close();
        }
        if (name != null && url != null) {
            //设置本地数据库的用户名和头像
            mDrawerName.setText(name);
            Glide.with(this).load(url).transform(new GlideRoundTransform(this)).into(mDrawerIcon);

        } else {
            mDrawerName.setText("登录");
            mDrawerIcon.setImageResource(R.mipmap.avatar_feedback);
        }
    }

    //加载布局
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    //设置toolbar
    private void setToolBar() {

        mMainBar.inflateMenu(R.menu.menu_title);

        //设置home菜单键的抽屉开关
        mMainBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        //设置溢出菜单图标
        Drawable drawable = getResources().getDrawable(R.mipmap.btn_home_more);
        mMainBar.setOverflowIcon(drawable);
        //toolbar子菜单项目的点击事件
        setMenuItem();
    }

    //设置toolbar子菜单项目的事件监听
    private void setMenuItem() {

        mMainBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_comment: //评论
//                        //设置弹窗效果
                        showPopupWindow(mMainBar);
                        break;
                    case R.id.menu_message:// 消息

                        break;
                    case R.id.menu_item_refresh: //刷新

                        break;
                    case R.id.menu_item_model: //模式

                        break;
                    case R.id.menu_item_setting: //设置

                        break;

                }

                return true;
            }
        });

    }

    //设置抽屉里面
    private void setDrawble() {
        //获取抽屉的头部视图
        View headerView = mNavigationDrawer.getHeaderView(0);
        //获取登录布局的对象
        View viewLayout = headerView.findViewById(R.id.layout_login);
        TextView textRead = (TextView) headerView.findViewById(R.id.text_read);
        TextView textComment = (TextView) headerView.findViewById(R.id.text_comment);
        TextView textCollect = (TextView) headerView.findViewById(R.id.text_collect);
        mDrawerIcon = (ImageView) viewLayout.findViewById(R.id.drawer_icon);
        mDrawerName = (TextView) viewLayout.findViewById(R.id.drawer_name);


        //设置头部登录视图的点击事件
        viewLayout.setOnClickListener(this);
        textRead.setOnClickListener(this);
        textCollect.setOnClickListener(this);


        //抽屉子菜单项目的点击事件
        mNavigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.menu_home: //首页
                        //设置首页显示
                        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                            mFrameLayout.setVisibility(View.GONE);
                            mDrawerLinear.setVisibility(View.VISIBLE);
                            count++;
                        }

                        //抽屉关闭
                        mDrawerLayout.closeDrawer(Gravity.LEFT);

                        break;
                    case R.id.menu_list: //排行榜
                        //实现排行榜的Fragment的动态添加
                        transaction.replace(R.id.drawer_frame, new RankFragment()).addToBackStack(null);

                        //设置最底层的activity不可见
                        mDrawerLinear.setVisibility(View.GONE);
                        //抽屉关闭
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        mFrameLayout.setVisibility(View.VISIBLE);

                        break;
                    case R.id.menu_channel:// 频道
                        //实现排行榜的Fragment的动态添加
                        transaction.replace(R.id.drawer_frame, new ChannelFragment()).addToBackStack(null);

                        //设置最底层的activity不可见
                        mDrawerLinear.setVisibility(View.GONE);
                        //抽屉关闭
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        mFrameLayout.setVisibility(View.VISIBLE);

                        break;
                    case R.id.menu_search: //搜索

//                        //实现排行榜的Fragment的动态添加
//                        transaction.replace(R.id.drawer_frame, new SearchFragment()).addToBackStack(null);
//                        //设置最底层的activity不可见
//                        mDrawerLinear.setVisibility(View.GONE);
//
//                        //抽屉关闭
//                        mDrawerLayout.closeDrawer(Gravity.LEFT);
//                        mFrameLayout.setVisibility(View.VISIBLE);


                        break;
                    case R.id.menu_stting: //设置

                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        //实现排行榜的Fragment的动态添加
//                        transaction.replace(R.id.drawer_frame, new SettingFragment()).addToBackStack(null);
//
//                        //设置最底层的activity不可见
//                        mDrawerLinear.setVisibility(View.GONE);
//
//                        //抽屉关闭
//                        mDrawerLayout.closeDrawer(Gravity.LEFT);
//                        mFrameLayout.setVisibility(View.VISIBLE);


                        break;
                    case R.id.menu_down: //离线下载
//
//                        //实现排行榜的Fragment的动态添加
//                        transaction.replace(R.id.drawer_frame, new DownFragment()).addToBackStack(null);
//                        //设置最底层的activity不可见
//                        mDrawerLinear.setVisibility(View.GONE);
//
//                        //抽屉关闭
//                        mDrawerLayout.closeDrawer(Gravity.LEFT);
//                        mFrameLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.menu_model: //夜间模式

                        break;

                }
                transaction.commit();
                return true;
            }
        });
    }

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.frament_layout, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        TextView textLink = (TextView) contentView.findViewById(R.id.text_link);

        TextView textArticle = (TextView) contentView.findViewById(R.id.text_article);

        textArticle.setOnClickListener(this);
        textLink.setOnClickListener(this);


        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框

        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                android.R.color.background_light));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }

    //初始化设置
    private void initSetting() {
        //获取tab的名称
        String[] array = getResources().getStringArray(R.array.tab_list);
        List<Fragment> fragments = new ArrayList<>();

        //创建Fragment，并且设置tab的名称
        for (int i = 0; i < array.length; i++) {
            //设置标题栏名称
            TabLayout.Tab tab = mMainTab.newTab();
            tab.setText(array[i]);
            mMainTab.addTab(tab);
            //创建fragment
            mMainTab.setTabTextColors(ColorStateList.valueOf(Color.RED));
            Fragment fragment = FragmentFactroy.createFragment(i);
            fragments.add(fragment);

        }

        //给viewpager关联适配器
        mMainPager.setAdapter(new MainAdapter(getSupportFragmentManager(), fragments, array));
    }

    //实现viewpager和tab的联动
    private void setPagerWithTab() {

        //切换viewpager，实现标题栏切换
        mMainPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMainTab)

        );

        //点击标题栏，实现viewpager切换
        mMainTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()

                                          {
                                              @Override
                                              public void onTabSelected(TabLayout.Tab tab) {
                                                  mMainPager.setCurrentItem(tab.getPosition(), false);
                                              }

                                              @Override
                                              public void onTabUnselected(TabLayout.Tab tab) {

                                              }

                                              @Override
                                              public void onTabReselected(TabLayout.Tab tab) {

                                              }
                                          }
        );
    }

    public void Draweropen() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }


    //设置点击事件监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_login:
                //跳转到登录界面
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.text_read: //阅读
                //  startActivity(new Intent(this, ReadActivity.class));
                break;
            case R.id.text_comment: //评论

                break;
            case R.id.text_collect:// 收藏
                startActivity(new Intent(this, CollectActivity.class));
                break;
            case R.id.text_link:// 推荐链接

                // startActivity(new Intent(this, CollectActivity.class));
                break;
            case R.id.text_article:// 投稿文章

                startActivity(new Intent(this, ArticleActivity.class));
                break;
        }
    }


    //处理返回键
    @Override
    public void onBackPressed() {

        //实现多功能切换Fragment 和Activity
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            count++;
            //当前Fragment的布局容器不可见
            mFrameLayout.setVisibility(View.GONE);
            //当前包裹抽屉外面的布局可见
            if (mDrawerLinear.getVisibility() == View.VISIBLE && count > 1) {
                //退出程序
                finish();
            }
            mDrawerLinear.setVisibility(View.VISIBLE);

        } else {
            //退出程序
            finish();
        }


    }


}
