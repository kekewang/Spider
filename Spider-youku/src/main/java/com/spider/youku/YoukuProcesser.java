package com.spider.youku;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangke on 2017/9/22.
 */
@Service
public class YoukuProcesser implements PageProcessor {

    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000).setTimeOut(10000);

    public List<Proxy> proxyList = new ArrayList();

    public BufferedReader proxyIpReader = new BufferedReader(new InputStreamReader(YoukuProcesser.class.getResourceAsStream("/config/proxyip.txt")));

    public static int pageCount = 0;

    public void process(Page page) {
        //http://v.youku.com/v_show/id_XMzA0OTI4NjcxMg==.html?spm=a2hww.20027244.m_250036.5~5!2~5~5!3~5~5~A
        page.addTargetRequests(page.getHtml().links().regex("//v.youku.com/v_show/id_\\w+.*").all());
        page.putField("url", page.getUrl().toString());
        page.putField("title", page.getHtml().xpath("//div[@class='base_info']/h1[@class='title']/allText()").toString());
        page.putField("category", page.getHtml().xpath("//h1[@class='title']/a/text()").toString());
        page.putField("categoryUrl", page.getHtml().xpath("//h1[@class='title']/a/@href"));
        page.putField("vid", page.getUrl().regex("v.youku.com/v_show/id_([\\w+]*)==").toString());


        if (page.getResultItems().get("vid") == null) {
            //skip this page
            page.setSkip(true);
        }
        System.out.println("=========================Page No." + pageCount++ + "=========================");
    }

    public Site getSite() {
        return site;
    }

    public ProxyProvider getSimpleProxyProvider() {
        String[] ip = new String[4];
        try {
            String socket = null;
            while ((socket = proxyIpReader.readLine()) != null) {
                String[] ipandport = socket.split(":");

                if (ipandport.length == 2) {
                    Proxy proxy = new Proxy(ipandport[0], Integer.parseInt(ipandport[1]));
                    proxyList.add(proxy);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        SimpleProxyProvider proxyProvider = new SimpleProxyProvider(proxyList);

        return proxyProvider;
    }

    public static void main(String[] args) {
        //ApplicationContext applicationContext = new

        YoukuProcesser processer = new YoukuProcesser();
        HttpClientDownloader downloader = new HttpClientDownloader();
        downloader.setProxyProvider(processer.getSimpleProxyProvider());
        Spider.create(processer).addUrl("http://www.youku.com/").setDownloader(downloader).thread(100).run();
    }
}
