package com.team.android_baozou.bean;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/13.
 * 创建一个EventBus接收传递的对象类
 */
public class BeanInfo {

    Map<String, String> mMap;


    public BeanInfo(Map<String, String> map) {
        this.mMap = map;
    }

    public Map<String, String> getUserMessage() {

        return mMap;
    }
}
