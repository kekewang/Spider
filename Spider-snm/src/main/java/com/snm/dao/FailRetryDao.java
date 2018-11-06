package com.snm.dao;

import com.snm.domain.FailedRecord;
import org.springframework.stereotype.Repository;

@Repository
public class FailRetryDao extends BaseDao<FailedRecord, Integer> {
}
