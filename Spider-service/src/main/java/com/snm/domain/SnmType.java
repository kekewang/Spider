package com.snm.domain;

import com.slob.dao.annotation.ID;
import com.slob.dao.annotation.Table;
import com.slob.dao.annotation.Column;
import java.lang.String;
import com.slob.core.Data;
import java.lang.Integer;

@Table(name = "snm_type")
public class SnmType extends BaseEntity implements Data<Integer> {
public static enum $ implements com.slob.core.Field {
	id, typeId, typeName;
}

private static final long serialVersionUID = -3444899715794526882L;

/*id*/
@ID
private Integer id;

/*类型id*/
@Column
private Integer typeId;

/*类型名*/
@Column
private String typeName;

public Integer getId() {
return this.id;
};

public void setId(Integer id) {
this.id = id;
};

public Integer getTypeId() {
return this.typeId;
};

public void setTypeId(Integer typeId) {
this.typeId = typeId;
};

public String getTypeName() {
return this.typeName;
};

public void setTypeName(String typeName) {
this.typeName = typeName;
};

}
