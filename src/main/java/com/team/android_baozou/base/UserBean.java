package com.team.android_baozou.base;

import java.util.List;

/**
 * Created by SoSho on 2016/10/11.
 */

public class UserBean {

    /**
     * document_id : 37307
     * display_type : 1
     * title : “就好像这个愚蠢的世界真有什么令人高兴的东西” | 那些让你嫉妒的快乐都是苦中作乐
     * comment_count : 4
     * vote_count : 5
     * contribute : 1
     * timestamp : 1476152559
     * url :
     * source_name :
     * hit_count : 161
     * hit_count_string : 161
     * publish_time : 1476138583000
     * published_at : 2016-10-11 06:29
     * recommenders : [{"id":544093,"name":"么么哒么么哒哎呀","avatar":"http://zhihu.b0.upaiyun.com/avatar/ee5a76864"}]
     * thumbnail : http://zhihu.b0.upaiyun.com/avatar/ee5a76864
     * author_id : 544093
     * author_name : 么么哒么么哒哎呀
     * author_avatar : http://zhihu.b0.upaiyun.com/avatar/ee5a76864
     */

    private int document_id;
    private int display_type;
    private String title;
    private int comment_count;
    private int vote_count;
    private int contribute;
    private int timestamp;
    private String url;
    private String source_name;
    private int hit_count;
    private String hit_count_string;
    private long publish_time;
    private String published_at;
    private String thumbnail;
    private int author_id;
    private String author_name;
    private String author_avatar;
    /**
     * id : 544093
     * name : 么么哒么么哒哎呀
     * avatar : http://zhihu.b0.upaiyun.com/avatar/ee5a76864
     */

    private List<RecommendersBean> recommenders;

    public int getDocument_id() {
        return document_id;
    }

    public void setDocument_id(int document_id) {
        this.document_id = document_id;
    }

    public int getDisplay_type() {
        return display_type;
    }

    public void setDisplay_type(int display_type) {
        this.display_type = display_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getContribute() {
        return contribute;
    }

    public void setContribute(int contribute) {
        this.contribute = contribute;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public int getHit_count() {
        return hit_count;
    }

    public void setHit_count(int hit_count) {
        this.hit_count = hit_count;
    }

    public String getHit_count_string() {
        return hit_count_string;
    }

    public void setHit_count_string(String hit_count_string) {
        this.hit_count_string = hit_count_string;
    }

    public long getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(long publish_time) {
        this.publish_time = publish_time;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
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

    public List<RecommendersBean> getRecommenders() {
        return recommenders;
    }

    public void setRecommenders(List<RecommendersBean> recommenders) {
        this.recommenders = recommenders;
    }

    public static class RecommendersBean {
        private int id;
        private String name;
        private String avatar;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
