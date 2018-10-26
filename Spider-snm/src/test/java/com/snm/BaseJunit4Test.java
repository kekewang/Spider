package com.snm;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * 规则包：
 * 规则编码：
 * 规则名称：
 * 规则条件：
 */
//让单元测试运行于spring环境，保证拥有spring框架相关支持
@RunWith(SpringJUnit4ClassRunner.class)
//加载spring容器
@ContextConfiguration(locations = {"classpath:/application.xml"})
public class BaseJunit4Test extends AbstractJUnit4SpringContextTests {
    protected TestContextManager testContextManager;
    @Before
    public void setUpContext() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
    }

    @Test
    public void registerDubbo() throws Exception {

        while (true);
    }
}