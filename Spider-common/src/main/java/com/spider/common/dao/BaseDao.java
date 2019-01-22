package com.spider.common.dao;

import com.slob.core.Data;
import com.slob.core.Field;
import com.slob.dao.DataRepository;
import com.slob.exception.SystemException;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

public class BaseDao<T extends Data<K>, K extends Serializable> {

    @Resource
    protected DataRepository dataRepository;

    public T getByKey(Class<T> entityClazz, K k) {
        return dataRepository.loadByKey(entityClazz, k);
    }

    public void deleteByKey(Class<T> entityClazz, K k) {
        dataRepository.deleteByKey(entityClazz, k);
    }

    public void delete(T entity) {
        dataRepository.delete(entity);
    }

    public void deleteAll(List<T> list) {
        dataRepository.delete(list);
    }

    public void save(T entity) {
        dataRepository.save(entity);
    }

    public void saveAll(List<T> list) {
        dataRepository.save(list);
    }

    public void update(T entity) {
        dataRepository.update(entity);
    }

    public void update(T entity, boolean ignoreNull) {
        dataRepository.update(entity, ignoreNull);
    }

    public void updateAll(List<T> list) {
        dataRepository.update(list);
    }

    public List<T> findAll(Class<T> entityClazz) {
        return dataRepository.loadAll(entityClazz);
    }

    public Field getFieldByName(Class<T> entityClazz, String fieldName) {
        try {
            Class fieldClass = Class.forName(entityClazz.getName() + "$$");
            Field field = (Field) Enum.valueOf(fieldClass, fieldName);

            return field;
        } catch (Exception e) {
            throw new SystemException("getFieldByName Error.", e.getMessage());
        }
    }
}
