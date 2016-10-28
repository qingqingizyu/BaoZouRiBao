package com.team.android_baozou.Thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.team.android_baozou.bean.PagerBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 类名称：
 * 作者：飞哥
 * 创建时间:2016/10/11 0011 19:38 .
 * 备注：
 */

public class JsonThreadList extends AsyncTask<String,Void,List<PagerBean>>{

    public interface OnGetJsonListResultLinstener{
        void OnGetJsonListResult(List<PagerBean> result);
    }

    OnGetJsonListResultLinstener mListener;

    public JsonThreadList(OnGetJsonListResultLinstener listener){
        this.mListener=listener;
    }

    @Override
    protected List<PagerBean> doInBackground(String... strings) {
        try {
            String HoneHttp = LoadHttpString(strings[0]);
            List<PagerBean> list = new ArrayList<>();
            JSONObject obj = new JSONObject(HoneHttp);
            JSONArray top_stories = obj.getJSONArray("top_stories");
            for (int i = 0; i < top_stories.length(); i++) {
                JSONObject object = top_stories.getJSONObject(i);
                String title = object.getString("title");
                String image = object.getString("image");
                String share_url = object.getString("share_url");
                byte[] bytes = LoadHttpBytes(image);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                PagerBean pagerBean = new PagerBean(title, bitmap,share_url);
                list.add(pagerBean);
            }
            return list;
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<PagerBean> pagerBeen) {
        super.onPostExecute(pagerBeen);
        if (mListener!=null && pagerBeen!=null) {
            mListener.OnGetJsonListResult(pagerBeen);
        }
    }

    public String LoadHttpString(String path) {
        try {
            Response response = new OkHttpClient().newCall(new Request.Builder().get().url(path).build()).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] LoadHttpBytes(String path) {

        try {
            Response response = null;
            response = new OkHttpClient().newCall(new Request.Builder().get().url(path).build()).execute();
            if (response.isSuccessful()) {
                return response.body().bytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
