package com.snm.domain;

import com.slob.dao.annotation.ID;
import com.slob.dao.annotation.Table;
import com.slob.dao.annotation.Column;
import com.slob.dao.keygenerator.SimpleKeyGenerator;
import java.lang.String;
import com.slob.core.Data;
import java.lang.Integer;

@Table(name = "snm_subtype")
public class SnmSubtype extends BaseEntity implements Data<Integer> {
public static enum $ implements com.slob.core.Field {
	id, subtypeParentId, subtypeId, subtypeName;
}

private static final long serialVersionUID = -2946204045201254333L;

/*id*/
@ID(keyType = SimpleKeyGenerator.class)
private Integer id;

/*父类型id*/
@Column
private Integer subtypeParentId;

/*类型id*/
@Column
private Integer subtypeId;

/*类型名*/
@Column
private String subtypeName;

public Integer getId() {
return this.id;
};

public void setId(Integer id) {
this.id = id;
};

public Integer getSubtypeParentId() {
return this.subtypeParentId;
};

public void setSubtypeParentId(Integer subtypeParentId) {
this.subtypeParentId = subtypeParentId;
};

public Integer getSubtypeId() {
return this.subtypeId;
};

public void setSubtypeId(Integer subtypeId) {
this.subtypeId = subtypeId;
};

public String getSubtypeName() {
return this.subtypeName;
};

public void setSubtypeName(String subtypeName) {
this.subtypeName = subtypeName;
};

}
