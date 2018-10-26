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

@Table(name = "snm_file")
public class SnmFile extends BaseEntity implements Data<Integer> {
    public static enum $ implements com.slob.core.Field {
        id, name, originName, path, updateTime, createTime;
    }

    private static final long serialVersionUID = -2172902721717306506L;

    /*文件id*/
    @ID
    private Integer id;

    /*文件名*/
    @Column
    private String name;

    /*文件原名*/
    @Column
    private String originName;

    /*目录*/
    @Column
    private String path;

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

    public String getOriginName() {
        return this.originName;
    }

    ;

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    ;

    public String getPath() {
        return this.path;
    }

    ;

    public void setPath(String path) {
        this.path = path;
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

}
