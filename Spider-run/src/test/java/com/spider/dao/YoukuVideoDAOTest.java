package com.spider.dao;

import com.spider.BaseSpringTest;
import com.spider.entity.YoukuVideoEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class YoukuVideoDAOTest extends BaseSpringTest {

    @Autowired
    YoukuVideoDAO youkuVideoDAO;

    @Test
    public void testInsert(){
        YoukuVideoEntity entity = new YoukuVideoEntity();
        entity.setTitle("视频标题");
        entity.setVid("310016511");
        entity.setCategory("影视");
        entity.setCategoryUrl("http://");
        entity.setUrl("http://");
        entity.setCreateTime("2017-11-22 22:20:14");
        entity.setUpdateTime("2017-11-22 22:20:14");
        youkuVideoDAO.insert(entity);
    }

    @Test
    public void testSelect(){
        List<YoukuVideoEntity> list = youkuVideoDAO.selectByKey("XMjc3MTAwNDc0MA");

        System.out.println(list);
    }

    @Test
    public void testUpdate(){
        YoukuVideoEntity entity = new YoukuVideoEntity();
        entity.setTitle("视频标题");
        entity.setCategory("影视");
        entity.setCategoryUrl("http://");
        entity.setUrl("http://");
        entity.setCreateTime("2017-11-22 22:20:14");
        entity.setUpdateTime("2017-11-22 22:20:14");
        youkuVideoDAO.updateByKey(entity);
    }
}