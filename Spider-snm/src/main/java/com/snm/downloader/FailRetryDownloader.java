package com.snm.downloader;

import com.snm.dao.FailRetryDao;
import com.snm.entity.FailedRecord;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.ProxyProvider;

import javax.annotation.Resource;

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
    }

    @Override
    protected void onError(Request request) {
        super.onError(request);

        FailedRecord failedRecord = new FailedRecord();
        failedRecord.setUrl(request.getUrl());
        failedRecord.setProcesser(this.pageProcessor.getClass().getName());

        failRetryDao.save(failedRecord);

    }
}
