package com.team.android_baozou.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.squareup.okhttp.Cache;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;


/**
 * Created by Administrator on 2016/9/23.
 */
public class OkHttpUtils {

    private static OkHttpClient mOkHttpClient;

    private static OkHttpUtils mOkHttpUtils;

    //创建OkHttpUtils的构造方法
    private OkHttpUtils(Context context) {
        mOkHttpClient = getOkHttpClient();
        mOkHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        int size = 1024 * 1024 * 10;//10M
        try {
            Cache cache = new Cache(context.getCacheDir(), size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //   利 用单例模式实现创建OkHttpClient的对象
    public static OkHttpClient getOkHttpClient() {

        if (mOkHttpClient == null) {
            synchronized (OkHttpClient.class) {
                mOkHttpClient = new OkHttpClient();
            }

        }
        return mOkHttpClient;
    }

    //利用单例模式获取OkHttpUtils的对象
    public static OkHttpUtils getOkHttpClientUtils(Context context) {

        if (mOkHttpUtils == null) {
            synchronized (OkHttpUtils.class) {
                mOkHttpUtils = new OkHttpUtils(context);
            }
        }
        return mOkHttpUtils;
    }

    // GET同步网络请求

    //得到一个Requset的对象
    private Request getRequest(String url) {
        Request request = new Request.Builder().get().url(url).build();
        return request;
    }

    //得到一个Response的对象
    private Response getResponse(String url) throws IOException {
        Request request = getRequest(url);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    //得到一个ResponseBody的对象
    private ResponseBody getResponseBody(String url) throws IOException {
        Response response = getResponse(url);
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            return body;
        }
        return null;
    }

    //获取Get同步的String 类型的数据
    public static String getOkHttpGetString(String url) throws IOException {
        //利用OkHttpUtils的对象调用gerRespnose方法

        ResponseBody requestBody = mOkHttpUtils.getResponseBody(url);

        return requestBody.string();
    }

    //返回值为byte[]类型
    public static byte[] getBytesFromUrl(String url) throws IOException {
        ResponseBody body = mOkHttpUtils.getResponseBody(url);
        if (body != null) {
            byte[] bytes = body.bytes();
            return bytes;
        }
        return null;
    }

    //返回值为字节流类型
    public static InputStream getStreamFromUrl(String url) throws IOException {
        ResponseBody body = mOkHttpUtils.getResponseBody(url);
        if (body != null) {
            InputStream stream = body.byteStream();
            return stream;
        }
        return null;
    }

    //获取Get异步的网络请求
    public static void getDataAsync(String url, Callback callback, Context context) {
        Request request = getOkHttpClientUtils(context).getRequest(url);
        mOkHttpClient.newCall(request).enqueue(callback);

    }


}
