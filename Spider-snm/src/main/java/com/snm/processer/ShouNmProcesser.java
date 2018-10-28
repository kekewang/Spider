package com.snm.processer;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import com.alibaba.fastjson.JSONObject;
import com.snm.component.ProxyComponent;
import com.snm.component.ShounimeiComponent;
import com.snm.dao.SnmDao;
import com.snm.dao.SnmFileDao;
import com.snm.dao.SnmTypeDao;
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
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jeanqinhu on 2017/8/22.
 */
@Service
public class ShouNmProcesser extends AbstractHttpClient implements PageProcessor {

    public static final Logger logger = LoggerFactory.getLogger(ShouNmProcesser.class);

    private Site site = Site.me().setRetryTimes(10).setTimeOut(1000);

    @Autowired
    private ProxyComponent proxyComponent;

    @Autowired
    private ShounimeiComponent shounimeiComponent;

    @Resource
    private SnmFileDao snmFileDao;

    @Resource
    private SnmDao snmDao;

    @Resource
    private SnmTypeDao snmTypeDao;

    public void process(Page page) {
        logger.info("Parsing page: {}", page.getUrl());
        try {
            page.addTargetRequests(page.getHtml().links().regex("(http://pt\\.aipt123.org/table_list\\.php?\\w+.*)").all());
            page.addTargetRequests(page.getHtml().links().regex("(http://pt\\.aipt123.org/details\\.php?\\w+.*)").all());
            List<String> targetLinks = page.getHtml().xpath("//a[starts-with(@href,'details.php')]/@href").all();
            page.addTargetRequests(targetLinks.stream().map((link) -> "http://pt.aipt123.org/"+link).collect(Collectors.toList()));

            String id = page.getUrl().regex("http://pt.aipt123.org/details.php[?&]id=([^&#]+)").toString();
            if (!StringUtils.isEmpty(id)) {
                page.putField(SpiderConstants.SNM_FILEID, id);
                page.putField(SpiderConstants.SNM_FILENAME, page.getHtml().xpath("//h1[@id='top']/text()").toString());
                List<String> propertys = page.getHtml().xpath("//td[@class='rowfollow']/text()").all();
                if (propertys.size() >= 2) {
                    String sizeTypeResolution = propertys.get(1);

                    page.putField(SpiderConstants.SNM_FILESIZE, SpiderUtils.getMatcher("^([0-9]*\\.[0-9]*\\s[M|G|T]B)", sizeTypeResolution));
                    page.putField(SpiderConstants.SNM_FILETYPE, SpiderUtils.getMatcher("([\\u4e00-\\u9fa5]{1,})\\s\\-", sizeTypeResolution));
                    page.putField(SpiderConstants.SNM_FILESUBTYPE, SpiderUtils.getMatcher("\\-\\s([\\u4e00-\\u9fa5]{1,})", sizeTypeResolution));
                }
                page.putField(SpiderConstants.SNM_DOWNLOADURL, String.format("http://pt.aipt123.org/download.php?id=%s&share_key=&qun_number=", id));

                //Download torrent
                SnmFile snmFile = shounimeiComponent.downloadTorrent(id);
                snmFileDao.save(snmFile);

                Snm snm = new Snm();
                snm.setFileId(id);
                snm.setName(page.getResultItems().get(SpiderConstants.SNM_FILENAME));
                snm.setDownloadUrl(page.getResultItems().get(SpiderConstants.SNM_DOWNLOADURL));
                snm.setPageUrl(page.getUrl().toString());
                snm.setSize(page.getResultItems().get(SpiderConstants.SNM_FILESIZE));
                TypeSubType typeSubType = snmTypeDao.loadTypesByName(page.getResultItems().get(SpiderConstants.SNM_FILETYPE),
                page.getResultItems().get(SpiderConstants.SNM_FILESUBTYPE));
                snm.setType(typeSubType.getTypeId());
                snm.setSubType(typeSubType.getSubtypeId());
                snm.setCreateTime(new Timestamp(new Date().getTime()));
                snm.setUpdateTime(new Timestamp(new Date().getTime()));
                logger.info(JSONObject.toJSONString(snm));
                snmDao.save(snm);
            }
        } catch (Exception e) {
            logger.error("Parsing page failed, url={}, {}", page.getUrl(), e.getMessage());
        }
    }

    public Site getSite() {
        try {
            if (shounimeiComponent.login()) {
                CookieStore cookieStore = shounimeiComponent.getContext().getCookieStore();
                //将获取到的cookie信息添加到webmagic中
                for (Cookie cookie : cookieStore.getCookies()) {
                    site.addCookie(cookie.getName().toString(), cookie.getValue().toString());
                }
            }
        } catch (Exception e) {
            throw new SpiderException("Login website failed！");
        }

        return site.setTimeOut(10000).setRetryTimes(3);
    }

    public void run() {
        logger.info("Starting spider...");
        HttpClientDownloader downloader = new HttpClientDownloader();
        //downloader.setProxyProvider(proxyComponent.getSimpleProxyProvider());
        Spider.create(this)
                .addUrl("http://pt.aipt123.org/table_list.php")
                //.setDownloader(downloader)
                .thread(50)
                .run();
    }
}
