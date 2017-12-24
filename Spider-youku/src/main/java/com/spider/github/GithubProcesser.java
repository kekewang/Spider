package com.spider.github;

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
 * Created by jeanqinhu on 2017/8/22.
 */
@Service
public class GithubProcesser implements PageProcessor {

    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000).setTimeOut(10000);

    @Autowired
    private ProxyComponent proxyComponent;

    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
        page.putField(SpiderConstants.GITHUB_AUTHOR, page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField(SpiderConstants.GITHUB_NAME, page.getHtml().xpath("//h1[@class='public']/strong/a/text()").toString());
        page.putField(SpiderConstants.GITHUB_ABOUT, page.getHtml().xpath("span[@itemprop='about']/text()"));
        page.putField(SpiderConstants.GITHUB_README, page.getHtml().xpath("//div[@id='readme']/tidyText()"));
        page.putField(SpiderConstants.GITHUB_FROK, page.getHtml().xpath("a[@class='social-count'][contains(@aria-label,'forked')]/text()"));
        page.putField(SpiderConstants.GITHUB_WATCH, page.getHtml().xpath("a[@class='social-count'][contains(@aria-label,'watch')]/text()"));
        page.putField(SpiderConstants.GITHUB_STAR, page.getHtml().xpath("a[@class='social-count'][contains(@aria-label,'star')]/text()"));

        if (page.getResultItems().get(SpiderConstants.GITHUB_NAME) == null) {
            //skip this page
            page.setSkip(true);
        }
        else {
            saveContent(page);
        }
    }

    private void saveContent(Page page){
        System.out.println(page.getResultItems().getAll());
    }

    public Site getSite() {
        return site;
    }

    public void run(){
        HttpClientDownloader downloader = new HttpClientDownloader();
        downloader.setProxyProvider(proxyComponent.getSimpleProxyProvider());
        Spider.create(this)
                .addUrl("https://github.com/search?q=wiki")
                .setDownloader(downloader)
                .thread(5)
                .run();
    }
}
