package com.spider.proxy.processer;

import com.spider.proxy.enums.ProxyType;
import com.spider.proxy.utils.ProxyValidater;
import com.spider.proxy.vo.ProxyVo;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.thread.CountableThreadPool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangke on 2017/9/22.
 */
public class XiciProcesser implements PageProcessor {

    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000).setTimeOut(10000);

    /**
     * Main page
     */
    private String xiciMainPage;

    /**
     * Page No.
     */
    private static int pageNo;

    public String getXiciMainPage() {
        return xiciMainPage;
    }

    public void setXiciMainPage(String xiciMainPage) {
        this.xiciMainPage = xiciMainPage;
        pageNo = 1;
    }

    public void process(Page page) {
        List<Selectable> nodes = page.getHtml().xpath("//table[@id='ip_list']/tbody/tr/allText()").nodes();
        for (int index=1; index<nodes.size(); index++){
            String proxyInfo = nodes.get(index).toString();
            String[] infos = proxyInfo.split(" ");
            if (infos.length>=8){
                ProxyVo proxyVo = new ProxyVo();
                proxyVo.setIp(infos[0]);
                proxyVo.setPort(infos[1]);
                proxyVo.setAddress(infos[2]);
                proxyVo.setAnonymous(infos[3]);
                proxyVo.setType(infos[4]);
                proxyVo.setAliveTime(infos[5]);
                proxyVo.setVerifyTime(infos[6] + " " + infos[7]);

                /*if (ProxyType.HTTP.getType().equalsIgnoreCase(infos[4]) &&
                    ProxyValidater.checkByProxychecker(proxyVo.getIp(),proxyVo.getPort())) {
                    System.out.println("Successed to add new proxy," + proxyVo.getIp() + ":" + proxyVo.getPort());
                    proxyList.add(proxyVo);

                    saveProxy("/proxy", proxyVo.getIp()+ ":" + proxyVo.getPort());
                }
                else {
                    System.out.println("Failed to add new proxy," + proxyVo.getIp() + ":" + proxyVo.getPort());
                }*/

                if (ProxyType.HTTP.getType().equalsIgnoreCase(infos[4])){
                    ProxyThreadPool.post(new Runnable() {
                        private String ip;

                        private String port;

                        @Override
                        public void run() {
                            boolean isValidProxy = ProxyValidater.checkByProxychecker(ip, port);
                            if (isValidProxy){
                                saveProxy("/proxy", ip + ":" + port);
                                System.out.println("Successed to add new proxy," + ip + ":" + port);
                            }
                            else {
                                System.out.println("Failed to add new proxy," + ","+ ip + ":" + port);
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
        }
        List links = new ArrayList<String>();
        if (pageNo<=5) {
            links.add(this.getXiciMainPage() + ++pageNo + "/");
            page.addTargetRequests(links);
        }
        //System.out.println(page.getUrl().toString());
    }

    public Site getSite() {
        return site;
    }

    public synchronized void saveProxy(String file, String proxy){
        String path = XiciProcesser.class.getResource(file).getPath();
        BufferedWriter proxyIpWriter = null;
        try {
            FileOutputStream outputFile = new FileOutputStream(path,true);
            OutputStreamWriter writer = new OutputStreamWriter(outputFile);
            proxyIpWriter = new BufferedWriter(writer);
            proxyIpWriter.write(proxy);
            proxyIpWriter.newLine();
            proxyIpWriter.flush();
            writer.close();
            proxyIpWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        String xici = "http://www.xicidaili.com/%s/";
        String[] types = {"wt","nn","wn","nt"};
        for (String type : types){
            String url = String.format(xici, type);
            XiciProcesser processor = new XiciProcesser();
            processor.setXiciMainPage(url);
            Spider.create(processor).addUrl(url).thread(20).run();
        }
    }
}
