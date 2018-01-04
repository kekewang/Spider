package com.spider.processer;

import com.spider.common.constant.SpiderConstants;
import com.spider.component.ProxyComponent;
import com.spider.component.ZhihuLoginComponent;
import com.spider.dao.ZhihuAnswerDAO;
import com.spider.dao.ZhihuArticleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangke on 2017/12/26.
 */
@Service
public class ZhihuProcesser implements PageProcessor {

    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .setCharset("UTF-8");

    @Autowired
    private ProxyComponent proxyComponent;

    @Autowired
    ZhihuAnswerDAO zhihuAnswerDAO;

    @Autowired
    ZhihuArticleDAO zhihuArticleDAO;

    @Autowired
    ZhihuLoginComponent zhihuLoginComponent;

    String regEx = "^/question/[0-9]*/answer/[0-9]*";
    private Pattern pattern = Pattern.compile(regEx);
    @Override
    public void process(Page page) {
        //https://www.zhihu.com/question/19677718
        List<String> relativeUrl = page.getHtml().xpath("//h2[@class='ContentItem-title']/div/a/@href").all();
        Iterator iterator = relativeUrl.iterator();
        while (iterator.hasNext()){
            String url = (String) iterator.next();
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                String[] urlArr = url.split("/");
                String origin = "http://www.zhihu.com/" + urlArr[1] + "/" + urlArr[2];
                relativeUrl.add(origin);
            }

            if (!url.contains("zhihu.com")){
                relativeUrl.remove(url);
            }
        }
        page.addTargetRequests(relativeUrl);
        page.putField(SpiderConstants.ZHIHU_ID, page.getUrl().regex("https://zhihu\\.com/(\\w+)/.*").toString());
        String title = page.getHtml().xpath("//div[@class='base_info']/h1[@class='title']/allText()").toString();

        int i = 0;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void run(){
        try {
            zhihuLoginComponent.login();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpClientDownloader downloader = new HttpClientDownloader();
        downloader.setProxyProvider(proxyComponent.getSimpleProxyProvider());
        Spider.create(this)
                .addUrl("http://www.zhihu.com/search?type=question&q=java")
                //.setDownloader(downloader)
                .thread(10)
                .run();
    }

}
