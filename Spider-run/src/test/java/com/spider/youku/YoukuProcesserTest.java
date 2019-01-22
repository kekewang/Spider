package com.spider.youku;

import com.spider.BaseSpringTest;
import com.spider.processer.YoukuProcesser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class YoukuProcesserTest extends BaseSpringTest {

    @Autowired
    YoukuProcesser youkuProcesser;

    @Test
    public void testProcess() throws Exception {
        youkuProcesser.run();
    }

    @Test
    public void start()
    {
        while (true);
    }
}