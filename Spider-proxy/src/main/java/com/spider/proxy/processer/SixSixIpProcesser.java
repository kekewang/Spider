package com.spider.proxy.processer;

import com.spider.proxy.domain.Proxy;
import com.spider.proxy.service.ProxyService;
import com.spider.proxy.utils.ProxyValidater;
import com.spider.proxy.vo.ProxyVo;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wangke on 2017/10/9.
 */
@Component
public class SixSixIpProcesser implements PageProcessor{

    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000).setTimeOut(10000);

    /**
     * Main page
     */
    private String mainPage;

    /**
     * Page No.
     */
    private static int pageNo;

    @Resource
    private ProxyService proxyService;

    private Set<String> proxySet = new HashSet<String>();

    public String get66IpMainPage() {
        return mainPage;
    }

    public void set66IpMainPage(String mainPage) {
        this.mainPage = mainPage;
        pageNo = 1;
    }

    @Override
    public void process(Page page) {
        List<Selectable> nodes = page.getHtml().xpath("//div[@id='footer']/div/table/tbody/tr/allText()").nodes();
        for (int index=1; index<nodes.size(); index++){
            String proxyInfo = nodes.get(index).toString();
            String[] infos = proxyInfo.split(" ");
            if (infos.length>=6){
                ProxyVo proxyVo = new ProxyVo();
                proxyVo.setIp(infos[0]);
                proxyVo.setPort(infos[1]);
                proxyVo.setAddress(infos[2]);
                proxyVo.setAnonymous(infos[3]);
                proxyVo.setVerifyTime(infos[4]);

                ProxyThreadPool.post(new Runnable() {
                    private String ip;

                    private String port;

                    @Override
                    public void run() {
                        boolean isValidProxy = ProxyValidater.checkProxy(ip, port);
                        String proxy = ip + ":" + port;
                        if (isValidProxy && !proxySet.contains(proxy)){
                            proxySet.add(proxy);
                            //FileUtil.saveProxy("/proxy", proxy);

                            Proxy proxy1 = new Proxy();
                            proxy1.setIp(ip);
                            proxy1.setPort(Integer.valueOf(port));
                            proxyService.saveProxy(proxy1);
                            System.out.println("Successed to add new proxy," + proxy);
                        }
                        else {
                            System.out.println("Failed to add new proxy," + ","+ proxy);
                        }
                    }

                    public Runnable init(String ip, String port) {
                        this.ip = ip;
                        this.port = port;
                        return this;
                    }
                }.init(proxyVo.getIp(), proxyVo.getPort()));

            }
        }
        List links = new ArrayList<String>();
        if (pageNo<=5) {
            links.add(String.format(this.get66IpMainPage(), ++pageNo));
            page.addTargetRequests(links);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args){
        String sixIp = "http://www.66ip.cn/areaindex_35/%d.html";

        SixSixIpProcesser processor = new SixSixIpProcesser();
        processor.set66IpMainPage(sixIp);
        String url = String.format(sixIp, 1);
        Spider.create(processor).addUrl(url).thread(20).run();
    }
}
