package com.team.android_baozou.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;

import com.team.android_baozou.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserVideo extends AppCompatActivity {

    @BindView(R.id.user_web)
    WebView userWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_video);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String id = intent.getStringExtra("userweb");
        String web= "http://baozouribao.com/documents/"+id;
        userWeb.loadUrl(web);
    }
}
