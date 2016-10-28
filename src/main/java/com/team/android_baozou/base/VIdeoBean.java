package com.team.android_baozou.base;

/**
 * Created by SoSho on 2016/10/11.
 */

public class VIdeoBean {

    /**
     * document_id : 37322
     * display_type : 3
     * play_time : 34
     * title : 女乒世界杯首个非中国球员冠军：日本00后，惊讶
     * image : http://baozouribao-qiniu.b0.upaiyun.com/ribaovideo/2016/10/1476153019573-zghk570wv2kh88zyuuq96n-b55d7db5cfd78943c1135f10a6b1abcb
     * play_count : 2
     * comment_count : 0
     * vote_count : 0
     * file_url : http://gslb.miaopai.com/stream/KE7YlU2JNmdnl-10pORWzQ__.mp4
     * share_url : http://baozouribao.com/documents/37322
     * publish_time : 1476183600000
     * play_count_string : 2
     */

    private int document_id;
    private int display_type;
    private int play_time;
    private String title;
    private String image;
    private int play_count;
    private int comment_count;
    private int vote_count;
    private String file_url;
    private String share_url;
    private long publish_time;
    private String play_count_string;

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

    public int getPlay_time() {
        return play_time;
    }

    public void setPlay_time(int play_time) {
        this.play_time = play_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPlay_count() {
        return play_count;
    }

    public void setPlay_count(int play_count) {
        this.play_count = play_count;
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

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public long getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(long publish_time) {
        this.publish_time = publish_time;
    }

    public String getPlay_count_string() {
        return play_count_string;
    }

    public void setPlay_count_string(String play_count_string) {
        this.play_count_string = play_count_string;
    }
}
