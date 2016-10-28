package com.team.android_baozou.drawer.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.team.android_baozou.R;
import com.team.android_baozou.base.BaseActivity;
import com.team.android_baozou.bean.BeanInfo;
import com.team.android_baozou.db.DBHelper;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class OtherLoginActivity extends BaseActivity {


    @BindView(R.id.login_toolbar)
    Toolbar mLoginToolbar;
    @BindView(R.id.login_qq)
    ImageView mLoginQq;
    @BindView(R.id.login_sina)
    ImageView mLoginSina;
    @BindView(R.id.login_wexin)
    ImageView mLoginWexin;

    private UMShareAPI mShareAPI;
    private SQLiteDatabase mDb;


    //初始化布局
    @Override
    protected void initView() {
        //初始化
        mShareAPI = UMShareAPI.get(this);


        //设置toolbar的返回
        setToolbar();
        DBHelper dbHelper = new DBHelper(this);

        mDb = dbHelper.getWritableDatabase();

    }


    //设置toolbar
    private void setToolbar() {
        mLoginToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //加载布局
    @Override
    public int getLayoutId() {
        return R.layout.activity_other_login;
    }


    //接口回掉结果
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data.get("screen_name") != null) {

                Log.e("TAG", data.get("screen_name"));
                //把网络的数据存储到数据库里
                ContentValues values = new ContentValues();
                values.put("name", data.get("screen_name"));
                values.put("url", data.get("profile_image_url"));
                mDb.insert("user", null, values);
                //传递数据到LoginActivity

            }
            Intent intent = new Intent(OtherLoginActivity.this, LoginActivity.class);
            EventBus.getDefault().post(new BeanInfo(data));
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "授权成功" + data.toString(), Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "取消登录", Toast.LENGTH_SHORT).show();
        }
    };

    @OnClick({R.id.login_qq, R.id.login_sina, R.id.login_wexin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_qq:
                //授权
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
                //获取用户的信息
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.login_sina:
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, umAuthListener);

                //获取用户信息
                // mShareAPI.getPlatformInfo(this, SHARE_MEDIA.SINA, umAuthListener);

                break;
            case R.id.login_wexin:
                //用戶授权
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                //获取用户的信息
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
        }
    }
}
