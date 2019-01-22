package com.spider.proxy.service.impl;

import com.spider.BaseSpringTest;
import com.spider.proxy.service.ProxyService;
import junit.framework.TestCase;
import org.junit.Test;

import javax.annotation.Resource;

public class ProxyServiceImplTest extends BaseSpringTest {

    @Resource
    ProxyService proxyService;

    @Test
    public void testSpiderProxy() throws Exception {

        proxyService.spiderProxy();
    }

}