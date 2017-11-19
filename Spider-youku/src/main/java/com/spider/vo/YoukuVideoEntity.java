package com.spider.vo;

import java.io.Serializable;

public class YoukuVideoEntity implements Serializable{

    /**
     * Video title
     */
    private String title;

    /**
     * Video category
     */
    private String category;

    /**
     * Category url
     */
    private String categoryUrl;

    /**
     * Video url
     */
    private String url;

    /**
     * Video id
     */
    private String vid;

    private String createTime;

    private String updateTime;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
