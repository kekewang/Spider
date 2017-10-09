package com.spider.proxy.processer;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.thread.CountableThreadPool;

/**
 * Created by wangke on 2017/10/9.
 */
public class ProxyValidator implements Runnable, Task {

    protected CountableThreadPool threadPool;

    @Override
    public void run() {

    }

    @Override
    public String getUUID() {
        return null;
    }

    @Override
    public Site getSite() {
        return null;
    }
}
