package com.snm.processer;

import com.snm.component.ShounimeiComponent;
import com.snm.dao.SnmSubtypeDao;
import com.snm.dao.SnmTypeDao;
import com.snm.entity.SnmSubtype;
import com.snm.entity.SnmType;
import com.snm.model.AbstractHttpClient;
import com.spider.common.exception.SpiderException;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeanqinhu on 2017/8/22.
 */
@Service
public class ShouNmTypeProcesser extends AbstractHttpClient implements PageProcessor {

    public static final Logger logger = LoggerFactory.getLogger(ShouNmTypeProcesser.class);

    private Site site = Site.me().setRetryTimes(10).setTimeOut(1000);

    @Autowired
    private ShounimeiComponent shounimeiComponent;

    @Resource
    public SnmTypeDao snmTypeDao;

    @Resource
    public SnmSubtypeDao snmSubtypeDao;

    public static Map<Integer, String> typeMap = new HashMap<>();
    public static Map<Integer, String> subtypeMap = new HashMap<>();

    public void process(Page page) {
        logger.info("Parsing page: {}", page.getUrl());
        try {
            page.addTargetRequests(page.getHtml().links().regex("(http://pt.aipt123.org/table_list.php\\?cat=[0-9]*$)").all());

            List<String> catIds = page.getHtml().xpath("//td[@class='bottom'][starts-with(@onmouseover,'showOrHideSearchboxScategory')]/input/@id").all();
            for (String id : catIds){
                id = id.substring(3, id.length());
                String xpathStr = String.format("//td[@class='bottom'][starts-with(@onmouseover,'showOrHideSearchboxScategory')]/a[contains(@href, '%s')]/img/@title", id);
                List<String> titles = page.getHtml().xpath(xpathStr).all();
                if (titles.size()>1){
                    int i=2;
                }
                Assert.assertTrue(titles.size()==1);
                String title = titles.get(0);

                int typeId = Integer.valueOf(id);
                if (!typeMap.containsKey(typeId)){
                    SnmType snmType = new SnmType();
                    snmType.setTypeId(typeId);
                    snmType.setTypeName(title);
                    snmTypeDao.save(snmType);
                    typeMap.put(typeId, title);
                }

                String subTypeXpath = String.format("//td[@class='bottom'][starts-with(@onmouseover,'showOrHideSearchboxScategory')]/div/ul/li/em/input[contains(@class, %s)]/@id", id);
                List<String> subTypeIds = page.getHtml().xpath(subTypeXpath).all();

                for (String subTypeId : subTypeIds){
                    subTypeId = subTypeId.substring(4, subTypeId.length());
                    String subXpathStr = String.format("//td[@class='bottom'][starts-with(@onmouseover,'showOrHideSearchboxScategory')]/div/ul/li/a[contains(@href, 'cat=%s')][ends-with(@href, 'scat=%s')]/text()", id, subTypeId);
                    List<String> subTitles  = page.getHtml().xpath(subXpathStr).all();
                    if (subTitles.size()>1){
                        int i=2;
                    }
                    Assert.assertTrue(subTitles.size()==1);
                    String subtypeTitle = subTitles.get(0);

                    int iSubTypeId = Integer.valueOf(subTypeId);
                    if (!subtypeMap.containsKey(iSubTypeId)){
                        SnmSubtype snmSubtype = new SnmSubtype();
                        snmSubtype.setSubtypeId(iSubTypeId);
                        snmSubtype.setSubtypeName(subtypeTitle);
                        snmSubtype.setSubtypeParentId(Integer.valueOf(id));
                        snmSubtypeDao.save(snmSubtype);

                        subtypeMap.put(iSubTypeId, subtypeTitle);
                    }
                }
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
        List<SnmType> snmTypes = snmTypeDao.findAll(SnmType.class);
        for (SnmType snmType : snmTypes)
            typeMap.put(snmType.getTypeId(), snmType.getTypeName());

        List<SnmSubtype> snmSubtypes = snmSubtypeDao.findAll(SnmSubtype.class);
        for (SnmSubtype snmSubtype : snmSubtypes)
            subtypeMap.put(snmSubtype.getSubtypeId(), snmSubtype.getSubtypeName());

        logger.info("Starting spider...");
        Spider.create(this)
                .addUrl("http://pt.aipt123.org/table_list.php")
                .thread(50)
                .run();
    }
}
