package com.spider.youku;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by wangke on 2017/9/22.
 */
public class YoukuProcesser implements PageProcessor {

    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000).setTimeOut(10000);

    public void process(Page page) {
        page.putField("href", page.getHtml().xpath("//div[@class='p-thumb']/a/@href").toString());
        page.putField("title", page.getHtml().xpath("//div[@class='p-thumb']/a/@title").toString());
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args){
        Spider.create(new YoukuProcesser()).addUrl("http://www.youku.com/").thread(5).run();
    }
}
