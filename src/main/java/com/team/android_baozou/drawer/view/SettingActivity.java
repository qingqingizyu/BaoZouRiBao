package com.team.android_baozou.drawer.view;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.team.android_baozou.R;
import com.team.android_baozou.base.BaseActivity;
import com.team.android_baozou.db.DBHelper;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.sett_toolbar)
    Toolbar settToolbar;
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.linear1)
    LinearLayout linear1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.linear2)
    LinearLayout linear2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    @BindView(R.id.linear3)
    LinearLayout linear3;
    private boolean change;
    private SQLiteDatabase mDb;
    private UMShareAPI mShareAPI;

    //初始化布局
    @Override
    protected void initView() {
        mShareAPI = UMShareAPI.get(this);
        settToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();


            }
        });
        change = true;
        linear1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (change) {
                    iv1.setImageResource(R.mipmap.toggle_off);
                    change = false;
                } else {
                    iv1.setImageResource(R.mipmap.toggle_on);
                    change = true;
                }
            }
        });
        linear2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (change) {
                    iv2.setImageResource(R.mipmap.toggle_off);
                    change = false;
                } else {
                    iv2.setImageResource(R.mipmap.toggle_on);
                    change = true;
                }
            }
        });
        linear3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (change) {
                    iv3.setImageResource(R.mipmap.toggle_off);
                    change = false;
                } else {
                    iv3.setImageResource(R.mipmap.toggle_on);
                    change = true;
                }
            }
        });
        //创建数据库对象
        DBHelper dbHelper = new DBHelper(this);
        mDb = dbHelper.getWritableDatabase();

    }

    //加载布局
    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting2;
    }

    //接口回掉结果
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            // Toast.makeText(getApplicationContext(), "解除授权", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            //  Toast.makeText(getApplicationContext(), "取消解除授权", Toast.LENGTH_SHORT).show();
        }
    };

    //设置退出登录
    public void exit(View view) {
        //删除数据
        mDb.execSQL("delete from user");
        //解除授权
        mShareAPI.deleteOauth(this, SHARE_MEDIA.SINA, umAuthListener);
        Toast.makeText(SettingActivity.this, "你已退出登录", Toast.LENGTH_SHORT).show();
    }
}
