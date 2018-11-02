package com.snm.processer;

import com.snm.BaseJunit4Test;
import junit.framework.TestCase;
import org.junit.Test;

import javax.annotation.Resource;

public class DoubanProcesserTest extends BaseJunit4Test {

    @Resource
    DoubanProcesser doubanProcesser;

    @Test
    public void testRun() throws Exception {
        doubanProcesser.run();
    }

}