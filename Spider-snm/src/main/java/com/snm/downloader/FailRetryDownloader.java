package com.snm.downloader;

import com.snm.dao.FailRetryDao;
import com.snm.domain.FailedRecord;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.ProxyProvider;

import javax.annotation.Resource;

/**
 * 失败处理类
 */
@Component
public class FailRetryDownloader extends HttpClientDownloader {

    @Resource
    FailRetryDao failRetryDao;

    private PageProcessor pageProcessor;

    private ProxyProvider proxyProvider;

    public FailRetryDownloader(){

    }

    public FailRetryDownloader(PageProcessor pageProcessor, ProxyProvider proxyProvider) {
        this.pageProcessor = pageProcessor;
        this.proxyProvider = proxyProvider;
        super.setProxyProvider(proxyProvider);
    }

    @Override
    protected void onError(Request request) {
        super.onError(request);

        FailedRecord failedRecord = new FailedRecord();
        failedRecord.setUrl(request.getUrl());
        failedRecord.setProcesser(this.pageProcessor.getClass().getName());
        failedRecord.setStatus(0);
        failedRecord.setTimes(0);

        failRetryDao.save(failedRecord);

    }

    public PageProcessor getPageProcessor() {
        return pageProcessor;
    }

    public void setPageProcessor(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
    }

    public ProxyProvider getProxyProvider() {
        return proxyProvider;
    }

    @Override
    public void setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
        super.setProxyProvider(proxyProvider);
    }
}
