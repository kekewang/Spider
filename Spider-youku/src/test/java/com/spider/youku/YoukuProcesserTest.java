package com.spider.youku;

import com.spider.BaseSpringTest;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

public class YoukuProcesserTest extends BaseSpringTest {

    @Autowired
    YoukuProcesser youkuProcesser;

    @Test
    public void testProcess() throws Exception {
        youkuProcesser.run();
    }
}