package com.team.android_baozou.base;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;


/**
 * Created by Administrator on 2016/9/23.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        UMShareAPI.get(this);
        /*全能插件全局化*/
        x.Ext.init(this);
        x.Ext.setDebug(true);

        //AppID,AppSecret
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        //新浪微博的回调地址
        Config.REDIRECT_URL = "www.baidu.com";
        initOkHttp();//   初始化OkHttpClient
    }

    //初始化OkHttpClient
    private void initOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();

    }


}
