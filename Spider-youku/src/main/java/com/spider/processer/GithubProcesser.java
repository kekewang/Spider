package com.spider.processer;

import com.spider.common.constant.SpiderConstants;
import com.spider.component.ProxyComponent;
import com.spider.dao.GithubProjectDAO;
import com.spider.entity.GuthubProjectEntity;
import com.spider.entity.YoukuVideoEntity;
import com.spider.spider.GithubSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * Created by jeanqinhu on 2017/8/22.
 */
@Service
public class GithubProcesser implements PageProcessor {

    private Site site = Site.me().setRetryTimes(10).setTimeOut(1000);

    @Autowired
    GithubProjectDAO githubProjectDAO;

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
        page.putField(SpiderConstants.GITHUB_URL, page.getUrl().toString());
        page.putField(SpiderConstants.GITHUB_INDEX, page.getResultItems().get(SpiderConstants.GITHUB_AUTHOR)
                + "_" + page.getResultItems().get(SpiderConstants.GITHUB_NAME));

        if (page.getResultItems().get(SpiderConstants.GITHUB_NAME) == null) {
            //skip this page
            page.setSkip(true);
        }
        else {
            saveContent(page);
        }
    }

    private void saveContent(Page page){
        try {
            GuthubProjectEntity guthubProjectEntity = new GuthubProjectEntity();
            guthubProjectEntity.setAbout(page.getResultItems().get(SpiderConstants.GITHUB_ABOUT).toString());
            guthubProjectEntity.setIndex(page.getResultItems().get(SpiderConstants.GITHUB_INDEX).toString());
            guthubProjectEntity.setAuthor(page.getResultItems().get(SpiderConstants.GITHUB_AUTHOR).toString());
            guthubProjectEntity.setName(page.getResultItems().get(SpiderConstants.GITHUB_NAME).toString());
            guthubProjectEntity.setReadme(page.getResultItems().get(SpiderConstants.GITHUB_README).toString());
            guthubProjectEntity.setUrl(page.getResultItems().get(SpiderConstants.GITHUB_URL).toString());
            guthubProjectEntity.setFork(page.getResultItems().get(SpiderConstants.GITHUB_FROK).toString());
            guthubProjectEntity.setStar(page.getResultItems().get(SpiderConstants.GITHUB_STAR).toString());
            guthubProjectEntity.setWatch(page.getResultItems().get(SpiderConstants.GITHUB_WATCH).toString());

            List<GuthubProjectEntity> list
                    = githubProjectDAO.selectByKey(guthubProjectEntity.getIndex());
            if (list.isEmpty()) {
                githubProjectDAO.insert(guthubProjectEntity);
            }
            else {
                githubProjectDAO.updateByKey(guthubProjectEntity);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public Site getSite() {
        return site;
    }

    public void run(){
        HttpClientDownloader downloader = new HttpClientDownloader();
        downloader.setProxyProvider(proxyComponent.getSimpleProxyProvider());
        Spider.create(this)
                .addUrl("https://github.com/search?q=java")
                //.setDownloader(downloader)
                .thread(1000)
                .run();
    }
}
