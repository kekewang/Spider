package com.spider.service.impl;

import com.spider.service.RefreshManager;
import com.spider.service.Refreshable;
import com.spider.processer.YoukuProcesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("spiderService")
@DependsOn("refreshManager")
public class SpiderService implements InitializingBean, Refreshable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpiderService.class);

    private static final int DELAY = 0;

    private int refreshInterval = 10;

    @Autowired
    private YoukuProcesser youkuProcesser;

    @Resource
    private RefreshManager refreshManager;

    @Override
    public void refresh() {
        youkuProcesser.run();
    }

    @Override
    public boolean isNeedRefresh() {
        return true;
    }

    @Override
    public int getIntervalSeconds() {
        return refreshInterval;
    }

    @Override
    public String getName() {
        return "Starting Spider...";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        refreshManager.addRefreshable(this, DELAY);
    }
}
