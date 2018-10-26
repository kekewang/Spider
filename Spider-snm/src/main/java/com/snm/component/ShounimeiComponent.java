package com.snm.component;

import com.snm.entity.SnmFile;
import com.snm.model.AbstractHttpClient;
import com.spider.common.utils.SpiderUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 收你妹PT
 */
@Component
public class ShounimeiComponent extends AbstractHttpClient {

    public static final Logger logger = LoggerFactory.getLogger(ShounimeiComponent.class);

    @Autowired
    private YudamaCaptcha yudamaCaptcha;

    @Value("${shounimei.account}")
    public String account;

    @Value("${shounimei.password}")
    public String password;

    @Value("${shounimei.captcha.path}")
    public String captchaPath;

    @Value("${shounimei.torrent.path}")
    public String torrentPath;

    private ShounimeiComponent() {
        super();
    }

    private String initLogin() throws IOException {
        logger.info("Initial login...");
        // 设置默认跳转以及存储cookie
        CloseableHttpClient httpClient = createSSLClientDefault();
        String loginUrl = "http://pt.aipt123.org/login.php";
        HttpPost httpPost = new HttpPost(loginUrl);

        // 设置请求头
        httpPost.setHeader("Accept",
                "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate");
        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
        String imagehash = null;
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost, context);
            HttpEntity httpEntity = response.getEntity();
            String html = null;
            if (httpEntity == null) {
                logger.error("请求失败");
            } else {

                byte[] bytes = new byte[1024 * 1024]; // 最大1M
                InputStream is = httpEntity.getContent();
                int offset = 0;
                int numRead = 0;
                while (offset < bytes.length
                        && (numRead = is.read(bytes, offset, bytes.length - offset)) != -1)
                    offset += numRead;
                html = new String(bytes, 0, offset, "utf-8");
                String charSet = getCharSet(html);
                if (charSet == null && !charSet.equals("utf8") && !charSet.equals("utf-8"))
                    html = new String(bytes, 0, offset, charSet);
                Document doc = Jsoup.parse(html);
                imagehash = doc.select("input[name=imagehash]").get(0).attr("value");
            }

        } finally {
            response.close();
        }
        logger.info("get imagehash, {}", imagehash);
        return imagehash;
    }

    public boolean login() throws IOException, InterruptedException {
        String imagehash = initLogin();
        String captchaUrl = "http://pt.aipt123.org/image.php?action=regimage&imagehash=";
        List<BasicNameValuePair> payload = new LinkedList<BasicNameValuePair>();
        payload.add(new BasicNameValuePair("username", account));
        payload.add(new BasicNameValuePair("password", password));
        payload.add(new BasicNameValuePair("imagestring", verifyCaptcha(captchaUrl + imagehash)));
        payload.add(new BasicNameValuePair("imagehash", imagehash));

        CloseableHttpClient closeableHttpClient = createSSLClientDefault();
        HttpResponse response = null;
        String loginUrl = "http://pt.aipt123.org/takelogin.php";
        HttpPost httpPost = new HttpPost(loginUrl);

        // 设置请求头
        httpPost.setHeader("Accept",
                "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpPost.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate");
        httpPost.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");

        RequestConfig requestConfig = null;
        requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(60000).setConnectTimeout(60000)
                .setSocketTimeout(60000).build();
        httpPost.setConfig(requestConfig);

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

        if (payload != null) {
            for (BasicNameValuePair basicNameValuePair : payload)
                formparams.add(basicNameValuePair);
        }
        logger.info("set formparams, {}", payload);
        UrlEncodedFormEntity entity = null;
        entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httpPost.setEntity(entity);
        response = closeableHttpClient.execute(httpPost, context);
        if (response.getStatusLine().getStatusCode() == 302) {
            logger.info("Login success!");
            return true;
        }

        logger.info("Login failed!");
        return false;
    }

    public static boolean isHttps(String url) {
        boolean result = false;

        Pattern p = Pattern.compile("https://");
        Matcher m = p.matcher(url);
        if (m.find())
            result = true;

        return result;
    }

    /**
     * 正则获取字符编码
     *
     * @param content
     * @return
     */
    private static String getCharSet(String content) {
        String regex = "charset=['\\\"]*(.+?)[ '\\\">]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find())
            return matcher.group(1);
        else
            return null;
    }

    private void downloadCaptcha(String apiurl) {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        try {
            url = new URL(apiurl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            fos = new FileOutputStream(captchaPath);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
            logger.info("Download captcha finished");
        } catch (Exception e) {
            logger.error("Download captcha failed. " + e.getMessage());
        } finally {
            try {
                fos.close();
                bis.close();
                httpUrl.disconnect();
            } catch (Exception e) {
                logger.error("Download captcha failed. " + e.getMessage());
            }
        }
    }

    public SnmFile downloadTorrent(String id) throws IOException {
        SnmFile snmFile = null;

        logger.info("Downloading torrent...");
        String torrentUrl = String.format("http://pt.aipt123.org/download.php?id=%s&share_key=&qun_number=", id);
        CloseableHttpClient httpClient = createSSLClientDefault();
        HttpGet httpGet = new HttpGet(torrentUrl);

        // 设置请求头
        httpGet.setHeader("Accept",
                "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet, context);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity == null) {
                logger.error("请求失败");
            } else {
                snmFile = new SnmFile();
                String fileName = "";
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    if (header.getName().equalsIgnoreCase("Content-Disposition")) {
                        String contentDisposition = header.getValue();
                        fileName = contentDisposition.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
                        fileName = new String(fileName.getBytes("ISO-8859-1"));
                        break;
                    }
                }
                snmFile.setOriginName(fileName);

                String newFileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
                snmFile.setName(newFileName);

                String responseString = new BasicResponseHandler().handleResponse(response);
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                String path = torrentPath + File.separator + year + File.separator + month;
                snmFile.setPath(path);
                snmFile.setCreateTime(new Timestamp(new Date().getTime()));
                snmFile.setUpdateTime(new Timestamp(new Date().getTime()));

                SpiderUtils.writeFile(newFileName
                        , path
                        , responseString.getBytes("ISO-8859-1"));
            }
        } finally {
            response.close();
        }

        return snmFile;
    }

    public String verifyCaptcha(String apiurl) throws IOException, InterruptedException {

        downloadCaptcha(apiurl);

        //等待1秒钟
        Thread.sleep(1000);

        return yudamaCaptcha.verifyCaptcha(captchaPath);
    }
}
