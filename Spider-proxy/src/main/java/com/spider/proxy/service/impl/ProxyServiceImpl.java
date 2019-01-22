package com.spider.proxy.service.impl;

import com.spider.proxy.dao.ProxyDao;
import com.spider.proxy.domain.Proxy;
import com.spider.proxy.processer.SixSixIpProcesser;
import com.spider.proxy.processer.XiciProcesser;
import com.spider.proxy.service.ProxyService;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;

@Service
public class ProxyServiceImpl implements ProxyService {

    @Resource
    SixSixIpProcesser sixSixIpProcesser;

    @Resource
    XiciProcesser xiciProcesser;

    @Resource
    ProxyDao proxyDao;

    @Override
    public void spiderProxy() {
        String sixIp = "http://www.66ip.cn/areaindex_35/%d.html";
        sixSixIpProcesser.set66IpMainPage(sixIp);
        String url = String.format(sixIp, 1);
        Spider.create(sixSixIpProcesser).addUrl(url).thread(20).run();


        String xici = "http://www.xicidaili.com/%s/";
        String[] types = {"wt","nn","wn","nt"};
        for (String type : types){
            String xiciurl = String.format(xici, type);
            xiciProcesser.setXiciMainPage(xiciurl);
            Spider.create(xiciProcesser).addUrl(xiciurl).thread(20).run();
        }
    }

    @Override
    public void saveProxy(Proxy proxy) {
        proxyDao.save(proxy);
    }
}
