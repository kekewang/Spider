package com.snm.processer;

import com.alibaba.fastjson.JSONObject;
import com.snm.component.ProxyComponent;
import com.snm.component.ShounimeiComponent;
import com.snm.dao.SnmDao;
import com.snm.dao.SnmFileDao;
import com.snm.dao.SnmTypeDao;
import com.snm.downloader.FailRetryDownloader;
import com.snm.entity.Snm;
import com.snm.entity.SnmFile;
import com.snm.model.AbstractHttpClient;
import com.snm.utils.StringUtils;
import com.snm.vo.TypeSubType;
import com.spider.common.constant.SpiderConstants;
import com.spider.common.exception.SpiderException;
import com.spider.common.utils.SpiderUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jeanqinhu on 2017/8/22.
 */
@Service
public class DoubanProcesser extends AbstractHttpClient implements PageProcessor {

    public static final Logger logger = LoggerFactory.getLogger(DoubanProcesser.class);

    @Autowired
    private ProxyComponent proxyComponent;

    @Autowired
    FailRetryDownloader failRetryDownloader;

    public void process(Page page) {
        logger.info("Parsing page: {}", page.getUrl());
        try {
            List<String> links = page.getHtml().links().regex(".*movie\\.douban\\.com/.*").all();
            page.addTargetRequests(links);

            List<String> targetLinks = page.getHtml().xpath("//a[starts-with(@href,'details.php')]/@href").all();
            page.addTargetRequests(targetLinks.stream().map((link) -> "http://pt.aipt123.org/"+link).collect(Collectors.toList()));

            String id = page.getUrl().regex("https://movie.douban.com/subject/([0-9]*)/.*").toString();
            String title = page.getHtml().xpath("//div[@id='content']/h1/span/text()").toString();
            if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(title)) {
                page.putField(SpiderConstants.DOUBAN_SUBJECT, id);
                page.putField(SpiderConstants.DOUBAN_TITLE, page.getHtml().xpath("//div[@id='content']/h1/span/text()").toString());
                page.putField(SpiderConstants.DOUBAN_YEAR, page.getHtml().xpath("//div[@id='content']/h1/span[@class='year']/text()").toString());
                page.putField(SpiderConstants.DOUBAN_MAINPIC, page.getHtml().xpath("//div[@id='mainpic']/a/img/@src").toString());

                List categorys = new ArrayList();
                List releaseDate = new ArrayList();
                List<String> attrs = page.getHtml().xpath("//div[@id='info']/span").all();
                for (String attr : attrs){
                    Document doc = Jsoup.parse(attr);
                    if (attr.contains("导演")){
                        page.putField(SpiderConstants.DOUBAN_DIRECTOR, doc.select("a").text());
                    }
                    if (attr.contains("编剧")){
                        page.putField(SpiderConstants.DOUBAN_SCENARIST, doc.select("a").text());
                    }
                    if (attr.contains("主演")){
                        page.putField(SpiderConstants.DOUBAN_ACTOR, doc.select("a").text());
                    }
                    if (doc.select("span[property=v:genre]").size()>0){
                        categorys.add(doc.select("span[property=v:genre]").text());
                    }
                    if (doc.select("span[property=v:runtime]").size()>0){
                        page.putField(SpiderConstants.DOUBAN_TIME, doc.select("span[property=v:runtime]").text());
                    }
                    if (doc.select("span[property=v:initialReleaseDate]").size()>0){
                        releaseDate.add(doc.select("span[property=v:initialReleaseDate]").text());
                    }
                }
                page.putField(SpiderConstants.DOUBAN_CATEGORY, categorys);
                page.putField(SpiderConstants.DOUBAN_RELEASETIME, releaseDate);
                page.putField(SpiderConstants.DOUBAN_SCORE, page.getHtml().xpath("//strong[@class='ll rating_num']/text()").toString());
                page.putField(SpiderConstants.DOUBAN_SUMMARY, page.getHtml().xpath("//span[@property='v:summary']/text()").toString());

            }
            else{

            }
        } catch (Exception e) {
            logger.error("Parsing page failed, url={}, {}, {}"
                    , page.getUrl()
                    , e.getMessage()
                    , page.getResultItems());
        }
    }

    public Site getSite() {
        return Site.me().setRetryTimes(10).setTimeOut(10000);
    }

    public void run() {
        logger.info("Starting spider...");
        FailRetryDownloader downloader = new FailRetryDownloader(this, proxyComponent.getSimpleProxyProvider());
        Spider.create(this)
                .setDownloader(downloader)
                .addUrl("https://movie.douban.com/")
                .thread(50)
                .run();
    }
}
