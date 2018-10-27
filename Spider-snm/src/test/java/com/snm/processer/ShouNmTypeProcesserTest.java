package com.snm.processer;

import com.snm.BaseJunit4Test;
import junit.framework.TestCase;
import org.junit.Test;

import javax.annotation.Resource;

public class ShouNmTypeProcesserTest extends BaseJunit4Test {

    @Resource
    ShouNmTypeProcesser shouNmTypeProcesser;

    @Test
    public void testRun() throws Exception {
        shouNmTypeProcesser.run();
    }

}