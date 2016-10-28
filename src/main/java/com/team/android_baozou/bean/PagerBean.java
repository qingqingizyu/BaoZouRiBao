package com.team.android_baozou.bean;

import android.graphics.Bitmap;

/**
 * 类描述：
 * 类名称：
 * 作者：飞哥
 * 创建时间:2016/10/11 0011 14:00 .
 * 备注：
 */

public class PagerBean {
    private String title;
    private Bitmap image;
    private String share_url;

    public PagerBean() {
    }

    public PagerBean(String title, Bitmap image, String share_url) {
        this.title = title;
        this.image = image;
        this.share_url = share_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
