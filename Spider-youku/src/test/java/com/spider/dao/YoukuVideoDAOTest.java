package com.spider.dao;

import com.spider.vo.YoukuVideoEntity;
import junit.framework.TestCase;
import org.springframework.beans.factory.annotation.Autowired;

public class YoukuVideoDAOTest extends TestCase {

    @Autowired
    YoukuVideoDAO youkuVideoDAO;

    public void testInsert(){
        YoukuVideoEntity entity = new YoukuVideoEntity();
        youkuVideoDAO.insert(entity);
    }
}