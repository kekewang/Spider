package com.spider.processer;

import com.spider.BaseSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by wangke on 2017/12/26.
 */
public class ZhihuProcesserTest extends BaseSpringTest {

    @Autowired
    ZhihuProcesser zhihuProcesser;

    @Test
    public void run() throws Exception {
        zhihuProcesser.run();
    }

}