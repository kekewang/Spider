package com.spider.component;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZhihuLoginComponent {

    @Autowired
    private PersistentCookieStore cookieStore;

    public static HttpClientContext context = null;

    public static RequestConfig requestConfig = null;

    public static CloseableHttpClient httpClient = null;

    public String account = "18820944351";

    public String password = "7632708wk";

    private ZhihuLoginComponent() throws IOException {
        context = HttpClientContext.create();

        // 设置默认跳转以及存储cookie
        httpClient = createSSLClientDefault();
        boolean isEmailLogin = false;
        Pattern p_email = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        //判断是否为邮箱登录
        if (p_email.matcher(account).find())
            isEmailLogin = true;

        String loginUrl = null;
        if (isEmailLogin)
            loginUrl = "https://www.zhihu.com/login/email";
        else
            loginUrl = "https://www.zhihu.com/login/phone_num";
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

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost, context);
            CookieStore cookieStore = context.getCookieStore();
            List<Cookie> cookies = cookieStore.getCookies();
            for (Cookie cookie : cookies) {
                System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
                cookieStore.addCookie(cookie);
            }
        } finally {
            response.close();
        }
    }

    public void login() throws IOException {

        boolean isEmailLogin = false;
        Pattern p_email = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        //判断是否为邮箱登录
        if (p_email.matcher(account).find())
            isEmailLogin = true;

        List<BasicNameValuePair> payload = new LinkedList<BasicNameValuePair>();
        if (isEmailLogin)
            payload.add(new BasicNameValuePair("email", account));
        else
            payload.add(new BasicNameValuePair("phone_num", account));
        payload.add(new BasicNameValuePair("captcha_type", "cn"));
        payload.add(new BasicNameValuePair("password", password));

        CloseableHttpClient closeableHttpClient = createSSLClientDefault();
        HttpResponse response = null;
        String loginUrl = null;
        if (isEmailLogin)
            loginUrl = "https://www.zhihu.com/login/email";
        else
            loginUrl = "https://www.zhihu.com/login/phone_num";
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
                .setConnectionRequestTimeout(1000).setConnectTimeout(1000)
                .setSocketTimeout(1000).build();
        httpPost.setConfig(requestConfig);

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

        if (payload != null) {
            for (BasicNameValuePair basicNameValuePair : payload)
                formparams.add(basicNameValuePair);
        }
        UrlEncodedFormEntity entity = null;
        entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httpPost.setEntity(entity);
        response = closeableHttpClient.execute(httpPost, context);
        Header[] allHeaders = response.getAllHeaders();
        for (Header header : allHeaders) {
            System.out.println(header.getName() + ":" + header.getValue());
        }
        HttpEntity httpEntity = response.getEntity();
        String html = null;
        if (httpEntity == null)
            System.err.println("登录失败");
        else {

            byte[] bytes = new byte[1024 * 1024]; // 最大1M
            InputStream is = httpEntity.getContent();
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) != -1)
                offset += numRead;
            html = new String(bytes, 0, offset, "utf-8");
            String charSet = getCharSet(html);
            if (charSet == null || charSet.equals("utf8") || charSet.equals("utf-8"))
                // 若是未解析到编码方式或编码方式为UTF8
                System.out.println(html);
            else
                html = new String(bytes, 0, offset, charSet);

            System.out.println(html.getBytes("UTF-8"));

        }

    }

    /**
     * 返回一个SSL连接的CloseableHttpClient实例
     *
     * @return
     */
    public CloseableHttpClient createSSLClientDefault() {
        try {
            // 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
            requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
                    .setConnectionRequestTimeout(60000).build();

            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                    null, new TrustStrategy() {
                        // 信任所有
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) throws CertificateException {
                            return true;
                        }
                    }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslContext);

            return HttpClients
                    .custom()
                    .setRedirectStrategy(new DefaultRedirectStrategy())
                    .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                    .setDefaultCookieStore(cookieStore)
                    .setSSLSocketFactory(sslsf)
                    .setDefaultRequestConfig(requestConfig)
                    .setDefaultSocketConfig(
                            SocketConfig.custom().setSoTimeout(5000).build())
                    .build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
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
}
