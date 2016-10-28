package com.team.android_baozou.main.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

import com.team.android_baozou.R;
import com.team.android_baozou.bean.HomeListBean;
import com.team.android_baozou.drawer.view.CollectActivity;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoWeb extends AppCompatActivity {

    @BindView(R.id._web)
    WebView Web;
    @BindView(R.id.activity_video_web)
    CoordinatorLayout activityVideoWeb;
    @BindView(R.id.floatButton)
    FloatingActionButton floatButton;
    @BindView(R.id.webView_toolbat)
    Toolbar toobar;
    private String web;
    private String logo;
    private String title;
    private String autorName;
    private String authorImage;
    private String sourceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_web);
        ButterKnife.bind(this);
        x.view().inject(this);
        Intent intent = getIntent();
        web = intent.getStringExtra("web");

        logo = intent.getStringExtra("logo");
        title = intent.getStringExtra("title");
        autorName = intent.getStringExtra("autorName");
        authorImage = intent.getStringExtra("authorImage");
        sourceName = intent.getStringExtra("sourceName");

        Web.loadUrl(web);
        initCode();
    }

    //收藏代码实现
    private void initCode() {
        /*处理toobar点击事件*/
        toobar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
          /*数据库设置*/
        DbManager.DaoConfig config = new DbManager.DaoConfig();
        //数据库名字
        config.setDbName("collect2.db");
        config.setDbVersion(1); /*版本号*/
        final DbManager db = x.getDb(config);
        //处理浮动按钮点击事件
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean isCheck = true;
                    /*判断是否重复*/
                    List<HomeListBean> mList = db.findAll(HomeListBean.class);

                    if (mList==null) {
                        //收藏实现
                        HomeListBean bean = new HomeListBean();
                        bean.setTitle(title);
                        bean.setAuthor_avatar(authorImage);
                        bean.setAuthor_name(autorName);
                        bean.setSource_name(sourceName);
                        bean.setThumbnail(logo);
                        bean.setUrl(web);
                        db.save(bean);
                    }else {
                       for (HomeListBean listBean : mList) {
                            if ((listBean.getTitle()).equals(title)) {
                                Toast.makeText(VideoWeb.this, "你已经收藏过了，请不要重复", Toast.LENGTH_SHORT).show();
                                isCheck=false;
                                break;
                            }
                        }
                        if (isCheck) {
                            //收藏实现
                            HomeListBean bean = new HomeListBean();
                            bean.setTitle(title);
                            bean.setAuthor_avatar(authorImage);
                            bean.setAuthor_name(autorName);
                            bean.setSource_name(sourceName);
                            bean.setThumbnail(logo);
                            bean.setUrl(web);
                            db.save(bean);
                        }

                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }

                //收藏提示
                Snackbar bar = Snackbar.make(view, "收藏成功", Snackbar.LENGTH_LONG).setAction("点击查看收藏", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转到收藏夹
                        Intent intent = new Intent(VideoWeb.this, CollectActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setActionTextColor(Color.YELLOW);
                //获取SnackBar的背景视图
                View v = bar.getView();
                v.setBackgroundColor(Color.parseColor("#E62715"));
                //不要忘记show出来
                bar.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
