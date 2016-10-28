package com.team.android_baozou.drawer.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.team.android_baozou.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RankActivity extends AppCompatActivity {

    @BindView(R.id.ra_toolbar)
    Toolbar raToolbar;
    @BindView(R.id.rank_web)
    WebView rankWeb;
    @BindView(R.id.activity_rank)
    RelativeLayout activityRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        rankWeb.loadUrl(url);
    }
}
