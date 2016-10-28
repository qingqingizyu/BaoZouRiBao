package com.team.android_baozou.Thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.team.android_baozou.bean.PagerBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.team.android_baozou.utils.OkHttpUtils.getOkHttpClient;

/**
 * 类描述：
 * 类名称：
 * 作者：飞哥
 * 创建时间:2016/10/11 0011 19:38 .
 * 备注：
 */

public class JsonThread extends AsyncTask<String, Void, List<PagerBean>> {


    private static ByteArrayOutputStream baos;
    private static String name = null;
    private Context mContext;

    public interface OnGetJsonResultLinstener {
        void OnGetJsonResult(List<PagerBean> result);
    }

    OnGetJsonResultLinstener mListener;

    public JsonThread(OnGetJsonResultLinstener listener, Context context) {
        this.mListener = listener;
        this.mContext=context;
    }

    @Override
    protected List<PagerBean> doInBackground(String... strings) {
        try {
            byte[] HoneHttp = loadData(strings[0]);

            List<PagerBean> list = new ArrayList<>();
            JSONObject obj = new JSONObject(new String(HoneHttp));
            JSONArray top_stories = obj.getJSONArray("top_stories");
            for (int i = 0; i < top_stories.length(); i++) {
                JSONObject object = top_stories.getJSONObject(i);
                String title = object.getString("title");
                String image = object.getString("image");
                String share_url = object.getString("share_url");
                byte[] bytes = loadData(image);
                //bitmap优化
                BitmapFactory.Options opts=new BitmapFactory.Options();
                opts.inPreferredConfig.compareTo(Bitmap.Config.RGB_565);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts);
                PagerBean pagerBean = new PagerBean(title, bitmap,share_url);
                list.add(pagerBean);
            }
            return list;
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<PagerBean> pagerBeen) {
        super.onPostExecute(pagerBeen);
        if (mListener != null && pagerBeen != null) {
            mListener.OnGetJsonResult(pagerBeen);
        }
    }

    public String LoadHttpString(String path) {
        try {
            OkHttpClient mOkHttpClient = new OkHttpClient();
            Response response = mOkHttpClient.newCall(new Request.Builder().get().url(path).build()).execute();
            mOkHttpClient = getOkHttpClient();
            mOkHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
            mOkHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
            mOkHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
            int size = 1024 * 1024 * 10;//10M
            try {
                Cache cache = new Cache(mContext.getCacheDir(), size);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            OkHttpClient mOkHttpClient = new OkHttpClient();
            Response response = mOkHttpClient.newCall(new Request.Builder().get().url(path).build()).execute();
            mOkHttpClient = getOkHttpClient();
            mOkHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
            mOkHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
            mOkHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
            int size = 1024 * 1024 * 10;//10M
            try {
                Cache cache = new Cache(mContext.getCacheDir(), size);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.isSuccessful()) {
                return response.body().bytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] loadData(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setDoInput(true);
        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        }
        return null;
    }

}
