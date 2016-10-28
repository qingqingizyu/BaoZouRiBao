package com.team.android_baozou.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13.
 */
public class UserBean implements Serializable{
    String imageUrl;
    String name;

    public UserBean() {
    }

    public UserBean(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
