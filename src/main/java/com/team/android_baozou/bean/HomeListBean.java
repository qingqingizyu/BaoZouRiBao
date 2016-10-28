package com.team.android_baozou.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 类描述：
 * 类名称：
 * 作者：飞哥
 * 创建时间:2016/10/12 0012 11:21 .
 * 备注：
 */
@Table(name = "collect")
public class HomeListBean {
    @Column(name = "_id",isId = true)
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "thumbnail")
    private String thumbnail;/*图片*/
    @Column(name = "author_name")
    private String author_name;
    @Column(name = "author_avatar")
    private String author_avatar;/*作者图片*/
    @Column(name = "source_name")
    private String source_name;
    @Column(name = "url")
    private String url;

    public HomeListBean() {
    }

    public HomeListBean(String title, String thumbnail, String author_name, String author_avatar, String source_name, String url) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.author_name = author_name;
        this.author_avatar = author_avatar;
        this.source_name = source_name;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_avatar() {
        return author_avatar;
    }

    public void setAuthor_avatar(String author_avatar) {
        this.author_avatar = author_avatar;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
