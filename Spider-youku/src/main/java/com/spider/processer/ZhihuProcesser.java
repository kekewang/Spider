package com.spider.processer;

import com.spider.common.constant.SpiderConstants;
import com.spider.component.ProxyComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by wangke on 2017/12/26.
 */
@Service
public class ZhihuProcesser implements PageProcessor {

    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000).setTimeOut(10000);


    @Autowired
    private ProxyComponent proxyComponent;

    @Override
    public void process(Page page) {
        //https://www.zhihu.com/question/19677718
        page.addTargetRequests(page.getHtml().links().regex("(https://www\\.zhihu\\.com/question/\\w+)").all());
        page.putField(SpiderConstants.ZHIHU_ID, page.getUrl().regex("https://zhihu\\.com/(\\w+)/.*").toString());
        String title = page.getHtml().xpath("//div[@class='base_info']/h1[@class='title']/allText()").toString();

        int i = 0;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void run(){
        HttpClientDownloader downloader = new HttpClientDownloader();
        downloader.setProxyProvider(proxyComponent.getSimpleProxyProvider());
        Spider.create(this)
                .addUrl("https://www.zhihu.com/")
                .setDownloader(downloader)
                .thread(100)
                .run();
    }

}
