package com.team.android_baozou.main.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 类描述：
 * 类名称：
 * 作者：飞哥
 * 创建时间:2016/10/12 0012 8:12 .
 * 备注：
 */

public class MyAdapter extends PagerAdapter {
    List<ImageView> images;

    public MyAdapter(List<ImageView> images) {
        this.images = images;
    }

    @Override
    public int getCount() {

        return Integer.MAX_VALUE;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(images.get(position % images.size()));
        return images.get(position % images.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //从容器中移除某个条目
        container.removeView(images.get(position % images.size()));
    }
}
