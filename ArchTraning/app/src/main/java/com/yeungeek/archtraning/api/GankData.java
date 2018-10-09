package com.yeungeek.archtraning.api;

/**
 * @author yangjian
 * @date 2018/10/09
 */

public class GankData {
    private String _id;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;

    public String get_id() {
        return _id;
    }

    public GankData set_id(String _id) {
        this._id = _id;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public GankData setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public GankData setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public GankData setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    public String getSource() {
        return source;
    }

    public GankData setSource(String source) {
        this.source = source;
        return this;
    }

    public String getType() {
        return type;
    }

    public GankData setType(String type) {
        this.type = type;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public GankData setUrl(String url) {
        this.url = url;
        return this;
    }

    public boolean isUsed() {
        return used;
    }

    public GankData setUsed(boolean used) {
        this.used = used;
        return this;
    }

    public String getWho() {
        return who;
    }

    public GankData setWho(String who) {
        this.who = who;
        return this;
    }
}
