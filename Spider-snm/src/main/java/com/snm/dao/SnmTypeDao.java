package com.snm.dao;

import com.slob.dao.criteria.Condition;
import com.snm.entity.Snm;
import com.snm.entity.SnmSubtype;
import com.snm.entity.SnmType;
import com.snm.utils.StringUtils;
import com.snm.vo.TypeSubType;
import org.junit.Assert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SnmTypeDao extends BaseDao<SnmType, Integer> {

    public TypeSubType loadTypesByName(String typeName, String subTypeName){
        TypeSubType typeSubType = new TypeSubType();
        if(StringUtils.isEmpty(typeName) || StringUtils.isEmpty(subTypeName))
            return typeSubType;

        Condition condition = Condition.identical();
        condition.andEq(SnmType.$.typeName, typeName, false);

        List<SnmType> snmTypes = dataRepository.loadByCondition(SnmType.class, condition);
        Assert.assertTrue(snmTypes.size() == 1);
        SnmType parent = snmTypes.get(0);
        typeSubType.setTypeId(parent.getTypeId());
        typeSubType.setTypeName(parent.getTypeName());

        Condition condition1 = Condition.identical();
        condition1.andEq(SnmSubtype.$.subtypeName, subTypeName, false);
        condition1.andEq(SnmSubtype.$.subtypeParentId, parent.getTypeId(), false);

        List<SnmSubtype> snmSubtypes = dataRepository.loadByCondition(SnmSubtype.class, condition1);
        Assert.assertTrue(snmSubtypes.size() == 1);
        SnmSubtype snmSubtype = snmSubtypes.get(0);

        typeSubType.setSubtypeId(snmSubtype.getSubtypeId());
        typeSubType.setSubtypeName(snmSubtype.getSubtypeName());

        return typeSubType;
    }
}
