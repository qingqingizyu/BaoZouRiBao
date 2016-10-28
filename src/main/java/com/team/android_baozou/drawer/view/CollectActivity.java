package com.team.android_baozou.drawer.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.team.android_baozou.R;
import com.team.android_baozou.bean.HomeListBean;
import com.team.android_baozou.main.adapter.HomeAdapter;
import com.team.android_baozou.main.view.VideoWeb;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

public class CollectActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener{

    private Toolbar toolbar;
    private ListView mLv;
    private List<HomeListBean> mList;
    private HomeAdapter adapter;
    private DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collect);
        initView();
        initToobar();
        initLookCollect();
    }




    /*初始化*/
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.Collect_toolbat);
        mLv = (ListView) findViewById(R.id.collect_listView);
        mLv.setOnItemClickListener(this);

    }

    /*toobar退回事件*/
    private void initToobar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /*提取收藏数据*/
    private void initLookCollect() {
        try {
            /*初始化数据库*/
            x.view().inject(this);
            DbManager.DaoConfig config=new DbManager.DaoConfig();
            config.setDbName("collect2.db");
            config.setDbVersion(1);
            db = x.getDb(config);
             /*提取数据*/
            mList = db.findAll(HomeListBean.class);
            adapter = new HomeAdapter();
            adapter.setData(mList);
            mLv.setAdapter(adapter);
        } catch (DbException e) {
            e.printStackTrace();
        }
        /*长按数据库删除操作*/
        mLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                try {

                     db.delete(mList.get(position));
                    mList = db.findAll(HomeListBean.class);
                    adapter.setData(mList);
                    mLv.setAdapter(adapter);
                } catch (DbException e) {
                    e.printStackTrace();
                }
               adapter.notifyDataSetChanged();
                return false;
            }
        });

    }
    /*实现点击跳转*/
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent=new Intent(this,VideoWeb.class);
        HomeListBean ListBean = mList.get(position);
        String url = ListBean.getUrl();
        intent.putExtra("web", url);
        intent.putExtra("logo",ListBean.getThumbnail());
        intent.putExtra("title",ListBean.getTitle());
        intent.putExtra("autorName",ListBean.getAuthor_name());
        intent.putExtra("authorImage",ListBean.getAuthor_avatar());
        intent.putExtra("sourceName",ListBean.getSource_name());

        startActivity(intent);
        finish();
    }

    public void CollectButton(View view) {
        try {
            db.delete(HomeListBean.class);
            mList = db.findAll(HomeListBean.class);
            adapter.setData(mList);
            mLv.setAdapter(adapter);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
