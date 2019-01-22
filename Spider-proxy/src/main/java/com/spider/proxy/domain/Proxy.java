package com.spider.proxy.domain;

import com.slob.core.Data;
import com.slob.dao.annotation.Column;
import com.slob.dao.annotation.ID;
import com.slob.dao.annotation.Table;
import com.spider.common.BaseEntity;

@Table(name = "proxy")
public class Proxy extends BaseEntity implements Data<Integer> {
public static enum $ implements com.slob.core.Field {
	id, ip, port, status;
}

private static final long serialVersionUID = 1L;

/**/
@ID
private Integer id;

/*代理ip*/
@Column
private String ip;

/*端口号*/
@Column
private Integer port;

/**/
@Column
private Integer status = 1;

public Integer getId() {
return this.id;
};

public void setId(Integer id) {
this.id = id;
};

public String getIp() {
return this.ip;
};

public void setIp(String ip) {
this.ip = ip;
};

public Integer getPort() {
return this.port;
};

public void setPort(Integer port) {
this.port = port;
};

public Integer getStatus() {
return this.status;
};

public void setStatus(Integer status) {
this.status = status;
};

}
