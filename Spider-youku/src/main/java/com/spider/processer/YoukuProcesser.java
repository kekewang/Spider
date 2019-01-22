package com.spider.processer;

import com.spider.common.constant.SpiderConstants;
import com.spider.component.ProxyComponent;
import com.spider.dao.YoukuVideoDAO;
import com.spider.entity.YoukuVideoEntity;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wangke on 2017/9/22.
 */
@Service
public class YoukuProcesser implements PageProcessor {

    private Site site = Site.me().setRetryTimes(10).setSleepTime(1000).setTimeOut(1000);


    public static int pageCount = 0;

    @Resource
    private YoukuVideoDAO youkuVideoDAO;

    @Autowired
    private ProxyComponent proxyComponent;

    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("//v.youku.com/v_show/id_\\w+.*").all());
        page.putField(SpiderConstants.YOUKU_URL, page.getUrl().toString());
        String title = page.getHtml().xpath("//div[@class='base_info']/h1[@class='title']/allText()").toString();
        page.putField(SpiderConstants.YOUKU_TITLE, title);
        page.putField(SpiderConstants.YOUKU_CATEGORY, page.getHtml().xpath("//h1[@class='title']/a/text()").toString());
        page.putField(SpiderConstants.YOUKU_CATEGORYURL, page.getHtml().xpath("//h1[@class='title']/a/@href"));
        page.putField(SpiderConstants.YOUKU_VID, page.getUrl().regex("v.youku.com/v_show/id_([\\w+]*)==").toString());

        if (page.getResultItems().get("vid") == null) {
            //skip this page
            page.setSkip(true);
        }
        else {
            saveVideo(page);
        }
        System.out.println("=========================Page No." + pageCount++ + "=========================");
    }

    public Site getSite() {
        return site;
    }

    public void saveVideo(Page page){

        YoukuVideoEntity youkuVideoEntity = new YoukuVideoEntity();
        youkuVideoEntity.setVid((String) page.getResultItems().get(SpiderConstants.YOUKU_VID));
        youkuVideoEntity.setTitle((String) page.getResultItems().get(SpiderConstants.YOUKU_TITLE));
        youkuVideoEntity.setUrl((String) page.getResultItems().get(SpiderConstants.YOUKU_URL));
        youkuVideoEntity.setCategory((String) page.getResultItems().get(SpiderConstants.YOUKU_CATEGORY));
        youkuVideoEntity.setCategoryUrl(String.valueOf(page.getResultItems().get(SpiderConstants.YOUKU_CATEGORYURL)));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        youkuVideoEntity.setCreateTime(df.format(new Date()));
        youkuVideoEntity.setUpdateTime(df.format(new Date()));

        List<YoukuVideoEntity> list = youkuVideoDAO.selectByKey(youkuVideoEntity.getVid());
        if (list.isEmpty()) {
            youkuVideoDAO.insert(youkuVideoEntity);
        }
        else {
            youkuVideoDAO.updateByKey(youkuVideoEntity);
        }
    }

    public void run(){
        HttpClientDownloader downloader = new HttpClientDownloader();
        downloader.setProxyProvider(proxyComponent.getSimpleProxyProvider());
        Spider.create(this)
                .addUrl("http://www.youku.com/")
                .setDownloader(downloader)
                .thread(10)
                .run();
    }
}
