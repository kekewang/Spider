package com.spider.proxy.processer;

import com.spider.common.http.AbstractHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class ProxyPool extends AbstractHttpClient {

    public void fetchProxy() throws IOException {
        CloseableHttpClient closeableHttpClient = createSSLClientDefault();
        HttpResponse response = null;
        HttpGet httpGet = new HttpGet("http://123.207.35.36:5010/get");

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

        System.out.println(responseString);
    }

    public static void main(String[] args){
        try {
            for (int i=0; i<50; i++)
                new ProxyPool().fetchProxy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
