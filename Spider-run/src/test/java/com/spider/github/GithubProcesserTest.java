package com.spider.github;

import com.spider.BaseSpringTest;
import com.spider.processer.GithubProcesser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GithubProcesserTest extends BaseSpringTest {

    @Autowired
    GithubProcesser githubProcesser;

    @Test
    public void testProcess() throws Exception {
        githubProcesser.run();
    }
}