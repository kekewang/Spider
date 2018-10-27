package com.snm.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.snm.model.AbstractHttpClient;
import com.snm.utils.StringUtils;
import com.spider.common.exception.SpiderException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import static org.apache.http.entity.ContentType.IMAGE_PNG;
import static org.apache.http.entity.ContentType.TEXT_PLAIN;

@Component
public class YudamaCaptcha extends AbstractHttpClient {
    public static final Logger logger = LoggerFactory.getLogger(YudamaCaptcha.class);

    @Value("${yundama.account}")
    public String username;

    @Value("${yundama.password}")
    public String password;

    private static String apiurl = "http://api.yundama.net:5678/api.php?method=upload";
    private static String resulturl = "http://api.yundama.net:5678/api.php?method=result&cid=";

    public YudamaCaptcha() {
        super();
    }

    public String verifyCaptcha(String captcha) throws IOException {

        CloseableHttpClient closeableHttpClient = createSSLClientDefault();
        HttpResponse response = null;
        HttpPost httpPost = new HttpPost(apiurl);

        // 设置请求头
        httpPost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        httpPost.setHeader("Origin", "http://www.yundama.com");
        httpPost.setHeader("Referer", "http://www.yundama.com/demo.html");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");

        RequestConfig requestConfig = null;
        requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(60000).setConnectTimeout(60000)
                .setSocketTimeout(60000).build();
        httpPost.setConfig(requestConfig);

        Charset utf8 = Charset.forName("UTF-8");
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .setCharset(utf8)
                .addTextBody("username", username, TEXT_PLAIN)
                .addTextBody("password", password, TEXT_PLAIN)
                .addTextBody("codetype", "1006", TEXT_PLAIN)
                .addTextBody("appid", "1", TEXT_PLAIN)
                .addTextBody("appkey", "22cc5376925e9387a23cf797cb9ba745", TEXT_PLAIN)
                .addTextBody("timeout", "60", TEXT_PLAIN)
                .addTextBody("version", "YAPI/WEB v1.0.0", TEXT_PLAIN)
                .addTextBody("showimage", "0", TEXT_PLAIN)
                .addBinaryBody("file", new File(captcha), IMAGE_PNG, "captcha.png").build();

        httpPost.setEntity(reqEntity);
        try {
            response = closeableHttpClient.execute(httpPost, context);
            String responseString = new BasicResponseHandler().handleResponse(response);
            logger.info("Upload captcha image, return " + responseString);
            JSONObject object = JSON.parseObject(responseString);
            if (object.containsKey("cid"))
                return handlerCaptchaText(object.getString("cid"));
        } finally {
            closeableHttpClient.close();
        }
        return "";
    }

    private String handlerCaptchaText(String cid){
        int ncount = 10;
        while (ncount-- > 0){
            String captcha = "";
            try {
                captcha = captchaText(cid);
                if (!StringUtils.isEmpty(captcha))
                    return captcha;

                Thread.sleep(1000);
            }
            catch (Exception e){
                logger.error("Resolve captcha failed.");
            }
        }

        throw new SpiderException("Resolve captcha failed.");
    }

    private String captchaText(String cid) throws IOException {
        CloseableHttpClient closeableHttpClient = createSSLClientDefault();
        HttpResponse response = null;
        HttpGet httpGet = new HttpGet(resulturl + cid);

        // 设置请求头
        httpGet.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        httpGet.setHeader("Origin", "http://www.yundama.com");
        httpGet.setHeader("Referer", "http://www.yundama.com/demo.html");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");

        RequestConfig requestConfig = null;
        requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(60000).setConnectTimeout(60000)
                .setSocketTimeout(60000).build();
        httpGet.setConfig(requestConfig);

        response = closeableHttpClient.execute(httpGet, context);
        String responseString = new BasicResponseHandler().handleResponse(response);

        logger.info("Upload captcha image, return " + responseString);
        JSONObject object = JSON.parseObject(responseString);
        if (object.containsKey("text"))
            return object.getString("text");

        return "";
    }
}
