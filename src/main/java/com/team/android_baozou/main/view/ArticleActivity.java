package com.team.android_baozou.main.view;

import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.team.android_baozou.R;
import com.team.android_baozou.utils.RichTextEditor;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ArticleActivity extends AppCompatActivity {

    @BindView(R.id.article_bar)
    Toolbar mArticleBar;
    @BindView(R.id.edit_article)
    EditText mEditArticle;
    @BindView(R.id.rich_editor)
    RichTextEditor mRichEditor;
    @BindView(R.id.image_album)
    ImageView mAlbum;
    @BindView(R.id.img_take_photo)
    ImageView mTakePhoto;
    private Unbinder unbinder;
    private Uri uri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_article);
        //绑定黄油刀
        unbinder = ButterKnife.bind(this);

        //设置Toolbar
        setToolbar();

    }

    //设置Toolbar
    private void setToolbar() {
        mArticleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑黄油刀
        unbinder.unbind();
    }

    @OnClick({R.id.image_album, R.id.img_take_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_album: //获取本地相册图片放置到富文本中
                openAlbum();

                break;
            case R.id.img_take_photo: //拍照放置图片到富文本中
                takePicture();

                break;
        }
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
                setImageView(picFileFullName);

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

                    // toast("获取图片成功，path=" + realPath);
                    //TODO:创建一个工作线程实现上传图片
                    //  workThread(realPath);
                    //设置图片
                    setImageView(realPath);
                } else {
                    Toast.makeText(ArticleActivity.this, "网络异常，图片上传失败", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    //设置头像
    private void setImageView(String realPath) {

        int degree = readPictureDegree(realPath);
        //根据不同角度设置拍照结果设置图片
        if (degree <= 0) {
            //放置图片
            mRichEditor.insertImage(realPath);


        } else {
            //放置图片
            mRichEditor.insertImage(realPath);

        }
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
}
