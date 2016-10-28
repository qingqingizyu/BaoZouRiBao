package com.team.android_baozou.bean;

/**
 * 类描述：
 * 类名称：
 * 作者：飞哥
 * 创建时间:2016/10/13 0013 8:50 .
 * 备注：
 */

public class ChannelBean {
    private String name; /*标题*/
    private String summary;/*内容*/
    private String thumbnail;/*图片*/

    public ChannelBean() {
    }

    public ChannelBean(String name, String summary, String thumbnail) {
        this.name = name;
        this.summary = summary;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
