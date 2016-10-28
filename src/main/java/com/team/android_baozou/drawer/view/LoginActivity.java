package com.team.android_baozou.drawer.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.team.android_baozou.R;
import com.team.android_baozou.base.BaseActivity;
import com.team.android_baozou.bean.BeanInfo;
import com.team.android_baozou.db.DBHelper;
import com.team.android_baozou.drawer.adapter.ListViewAdapter;
import com.team.android_baozou.utils.GlideRoundTransform;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

//登录Activity
public class LoginActivity extends BaseActivity {

    //布局
    @BindView(R.id.linear_layout)
    LinearLayout linear_layout;
    //Toolbar
    @BindView(R.id._toolbar)
    Toolbar toolbar;

    //任务列表
    @BindView(R.id.task_lv)
    ListView lvTask;

    @BindView(R.id.image_login)
    ImageView mImageLogin;
    @BindView(R.id.text_login)
    TextView mTextLogin;
    private Uri uri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private SQLiteDatabase mDb;
    private UMShareAPI mShareAPI;

    //初始操作
    @Override
    protected void initView() {

        //初始化
        mShareAPI = UMShareAPI.get(this);

        //初始化EventBus
        EventBus.getDefault().register(this);
        toolbar.inflateMenu(R.menu.toobar_menu);
        //当SDK版本号大于5.0的时候
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置Toolbar按钮的图标
            toolbar.setOverflowIcon(getDrawable(R.mipmap.btn_home_more));
        }
        //设置listview布局
        setListView();

        //设置返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置头像
        setIcon();


    }


    //设置头像
    private void setIcon() {
        String name = null;
        String url = null;
        DBHelper dbHelper = new DBHelper(this);
        mDb = dbHelper.getWritableDatabase();

        Cursor cursor = mDb.rawQuery("select*from user", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex("name"));
                url = cursor.getString(cursor.getColumnIndex("url"));
            }
            //记得关流防止OOM
            cursor.close();
        }
        if (url != null && name != null) {
            //设置图片和用户姓名
            mTextLogin.setText(name);

            Glide.with(this).load(url).transform(new GlideRoundTransform(this)).into(mImageLogin);

        }

    }

    //设置listview布局
    private void setListView() {
        //设置任务列表里的图片集合
        int[] imageId = new int[]{R.mipmap.self_sign, R.mipmap.self_article, R.mipmap.self_comment,
                R.mipmap.self_article_like, R.mipmap.self_comment_like, R.mipmap.self_share};

        //任务名称
        String[] title = new String[]{"每日签到", "阅读文章", "发表评论", "点赞文章", "点赞评论", "分享文章"};

        //金币数量
        String[] count = new String[]{"+ 5至15", "+ 5", "+ 10", "+ 5", "+ 5", "+ 10"};

        //添加至集合
        List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < title.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", imageId[i]);
            map.put("title", title[i]);
            map.put("count", count[i]);
            listitem.add(map);
        }

        ListViewAdapter adapter = new ListViewAdapter(this, listitem);
        lvTask.setAdapter(adapter);

        //设置未登录状态

        lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.equals(mTextLogin.getText(), ("点击登录"))) {
                    Toast.makeText(LoginActivity.this, "请登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setUseMessage(BeanInfo info) {
        mShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, umAuthListener);
        mShareAPI.getPlatformInfo(this, SHARE_MEDIA.SINA, umAuthListener);
        //订阅获取的数据
        Map<String, String> userMessage = info.getUserMessage();
        String profile_image_url = userMessage.get("profile_image_url");
        String screen_name = userMessage.get("screen_name");
        if (profile_image_url != null && screen_name != null) {
            mTextLogin.setText(screen_name);
            Glide.with(this).load(profile_image_url).transform(new GlideRoundTransform(this)).into(mImageLogin);
        }

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

            }
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


    //加载布局
    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @OnClick({R.id.image_login, R.id.text_login})
    public void onClick(View view) {
        //判断当前是否登录
        if (TextUtils.equals(mTextLogin.getText(), ("点击登录"))) {
            //未登录情况
            Intent intent = new Intent(LoginActivity.this, OtherLoginActivity.class);
            startActivity(intent);

        } else {
            //登录的状态
            showPopupWindow(view);
        }
    }

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.icon_popwindow, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //设置弹窗的项目点击事件
        setPopupMenuItem(contentView);
        popupWindow.setTouchable(true);

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
                android.R.drawable.editbox_background));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }


    //设置弹窗的项目点击事件
    private void setPopupMenuItem(View view) {
        Button photoBut = (Button) view.findViewById(R.id.take_photo);
        Button ablumBut = (Button) view.findViewById(R.id.local_ablum);
        //设置拍照片，打开相机
        photoBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //照相
                takePicture();

            }
        });
        //设置获取本地相册
        ablumBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开本地相册
                openAlbum();
            }
        });

    }

    //开启一个工作线程传递路径
    private void workThread(final String path) {
        //开启一个工作线程
        new Thread() {
            @Override
            public void run() {
                //创建一个OkHttpClient的对象
                OkHttpClient okHttp = new OkHttpClient();

                try {
                    //图片对应的类型：image/*  文本对应的类型:text/*   音频对应的类型：audio/*   视频对应的类型：video/*
                    MediaType type = MediaType.parse("image/*");
                    //图片路径

                    File file = new File(path);
                    //得到一个RequestBody的对象
                    RequestBody body = RequestBody.create(type, file);
                    //得到一个Request的对象
                    Request request = new Request.Builder().url("服务器地址").post(body).build();
                    //得到一个Response的响应
                    final Response response = okHttp.newCall(request).execute();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static String picFileFullName;

    //拍照
    public void takePicture() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
            picFileFullName = outFile.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else {
            //  Log.e(tag, "请确认已经插入SD卡");
        }
    }

    //打开本地相册
    public void openAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {  // //获取拍照后的回掉结果
            if (resultCode == RESULT_OK) {
                //  Log.e(tag, "获取图片成功，path=" + picFileFullName);
                toast("获取图片成功，path=" + picFileFullName);
                if (picFileFullName != null) {
                    //把拍照头像保存到本地数据库中
                    ContentValues values = new ContentValues();
                    values.put("name", mTextLogin.getText().toString());
                    values.put("url", picFileFullName);
                    mDb.insert("user", null, values);
                    setImageView(picFileFullName);
                }

            } else if (resultCode == RESULT_CANCELED) {
                // 用户取消了图像捕获
            } else {
                // 图像捕获失败，提示用户
                //  Log.e(tag, "拍照失败");
            }
        } else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {  //获取打开相册回掉的图片
            if (resultCode == RESULT_OK) {
                uri = data.getData();
                if (uri != null) {
                    //得到图片的路径
                    String realPath = getRealPathFromURI(uri);
                    ContentValues values = new ContentValues();
                    values.put("name", mTextLogin.getText().toString());
                    values.put("url", realPath);
                    mDb.insert("user", null, values);
                    toast("获取图片成功，path=" + realPath);
                    //TODO:创建一个工作线程实现上传图片
                    //  workThread(realPath);
                    //设置本地相册的图片设为头像
                    setImageView(realPath);
                } else {
                    Toast.makeText(LoginActivity.this, "网络异常，图片上传失败", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    //设置头像
    private void setImageView(String realPath) {

        int degree = readPictureDegree(realPath);
        //根据不同角度设置拍照结果设置在头像位置
        if (degree <= 0) {
            //设置头像
            Glide.with(this).load(realPath).transform(new GlideRoundTransform(this)).into(mImageLogin);


        } else {
            //设置头像
            Glide.with(this).load(realPath).transform(new GlideRoundTransform(this)).into(mImageLogin);
        }
        //else {
//          //  Log.e(tag, "rotate:" + degree);
//            //创建操作图片是用的matrix对象
//            Matrix matrix = new Matrix();
//            //旋转图片动作
//            matrix.postRotate(degree);
//            //创建新图片
//            Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
//          //  imageView.setImageBitmap(resizedBitmap);
//        }
    }

    //获取图片地址
    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    /**
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        EventBus.getDefault().unregister(this);
    }
}