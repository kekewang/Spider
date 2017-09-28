package com.spider.youku;

import com.spider.utils.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by wangke on 2017/9/22.
 */
public class YoukuProcesser implements PageProcessor {

    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000).setTimeOut(10000);

    public static int pageCount = 0;

    public void process(Page page) {
        //http://v.youku.com/v_show/id_XMzA0OTI4NjcxMg==.html?spm=a2hww.20027244.m_250036.5~5!2~5~5!3~5~5~A
        page.addTargetRequests(page.getHtml().links().regex("//v.youku.com/v_show/id_\\w+.*").all());
        page.putField("url", page.getUrl().toString());
        page.putField("title", page.getHtml().xpath("//div[@class='base_info']/h1[@class='title']/allText()").toString());
        page.putField("class",page.getHtml().xpath("//h1[@class='title']/a/text()").toString());
        page.putField("classUrl", page.getHtml().xpath("//h1[@class='title']/a/@href"));
        page.putField("vid",page.getUrl().regex("v.youku.com/v_show/id_([\\w+]*)==").toString());

        if (page.getResultItems().get("vid") == null){
            //skip this page
            page.setSkip(true);
        }
        System.out.println("=========================Page No." + pageCount++ + "=========================");
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args){
        Spider.create(new YoukuProcesser()).addUrl("http://www.youku.com/").thread(20).run();
    }
}
