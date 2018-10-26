package com.snm.processer;

import com.snm.BaseJunit4Test;
import org.junit.Test;

import javax.annotation.Resource;

public class ShouNmProcesserTest extends BaseJunit4Test {

    @Resource
    ShouNmProcesser shouNmProcesser;

    @Test
    public void testRun() throws Exception {
        shouNmProcesser.run();
    }
}