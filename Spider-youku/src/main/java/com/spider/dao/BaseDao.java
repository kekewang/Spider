package com.spider.dao;

import java.util.List;

public interface BaseDao<PK,T> {

    void insert(T value);

    List<T> selectByKey(PK key);

    void deleteByKey(PK key);

    void updateByKey(T value);
}
