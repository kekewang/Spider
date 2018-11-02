package com.snm.dao;

import com.snm.entity.FailedRecord;
import com.snm.entity.Snm;
import org.springframework.stereotype.Repository;

@Repository
public class FailRetryDao extends BaseDao<FailedRecord, Integer> {
}
