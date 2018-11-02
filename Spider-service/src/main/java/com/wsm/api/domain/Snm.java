package com.snm.entity;

import com.slob.dao.annotation.ID;
import com.slob.dao.annotation.Table;
import com.snm.entity.BaseEntity;
import com.slob.dao.annotation.Column;

import java.sql.Timestamp;
import java.util.Date;
import java.lang.String;

import org.apache.ibatis.type.DateTypeHandler;
import com.slob.core.Data;
import org.apache.ibatis.type.SqlTimestampTypeHandler;

import java.lang.Integer;

@Table(name = "snm")
public class Snm extends BaseEntity implements Data<Integer> {
    public static enum $ implements com.slob.core.Field {
        id, name, type, subType, size, pageUrl, downloadUrl, fileId, description, updateTime, createTime;
    }

    private static final long serialVersionUID = -5409279253665948533L;

    /*资源id*/
    @ID
    private Integer id;

    /*资源名*/
    @Column
    private String name;

    /*资源类型*/
    @Column
    private Integer type;

    /*资源子类型*/
    @Column
    private Integer subType;

    /**/
    @Column
    private String size;

    /**/
    @Column
    private String pageUrl;

    /**/
    @Column
    private String downloadUrl;

    /**/
    @Column
    private String fileId;

    /**/
    @Column
    private String description;

    /**/
    @Column(typeHandlerType = SqlTimestampTypeHandler.class)
    private Timestamp updateTime;

    /**/
    @Column(typeHandlerType = SqlTimestampTypeHandler.class)
    private Timestamp createTime;

    public Integer getId() {
        return this.id;
    }

    ;

    public void setId(Integer id) {
        this.id = id;
    }

    ;

    public String getName() {
        return this.name;
    }

    ;

    public void setName(String name) {
        this.name = name;
    }

    ;

    public Integer getType() {
        return this.type;
    }

    ;

    public void setType(Integer type) {
        this.type = type;
    }

    ;

    public String getSize() {
        return this.size;
    }

    ;

    public void setSize(String size) {
        this.size = size;
    }

    ;

    public String getPageUrl() {
        return this.pageUrl;
    }

    ;

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    ;

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    ;

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    ;

    public String getFileId() {
        return this.fileId;
    }

    ;

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    ;

    public String getDescription() {
        return this.description;
    }

    ;

    public void setDescription(String description) {
        this.description = description;
    }

    ;

    public Timestamp getUpdateTime() {
        return this.updateTime;
    }

    ;

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    ;

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    ;

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    ;


    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }
}
