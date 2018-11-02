package com.snm.entity;

import com.slob.dao.annotation.ID;
import com.slob.dao.annotation.Table;
import com.snm.entity.BaseEntity;
import com.slob.dao.annotation.Column;
import java.lang.String;
import com.slob.core.Data;
import java.lang.Integer;

@Table(name = "failed_record")
public class FailedRecord extends BaseEntity implements Data<Integer> {
public static enum $ implements com.slob.core.Field {
	id, url, processer, status, times;
}

private static final long serialVersionUID = -759716053940700438L;

/*id*/
@ID
private Integer id;

/*失败地址*/
@Column
private String url;

/*处理器*/
@Column
private String processer;

/*状态*/
@Column
private Integer status;

/*重试次数*/
@Column
private Integer times;

public Integer getId() {
return this.id;
};

public void setId(Integer id) {
this.id = id;
};

public String getUrl() {
return this.url;
};

public void setUrl(String url) {
this.url = url;
};

public String getProcesser() {
return this.processer;
};

public void setProcesser(String processer) {
this.processer = processer;
};

public Integer getStatus() {
return this.status;
};

public void setStatus(Integer status) {
this.status = status;
};

public Integer getTimes() {
return this.times;
};

public void setTimes(Integer times) {
this.times = times;
};

}
