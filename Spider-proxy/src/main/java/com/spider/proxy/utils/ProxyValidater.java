package com.spider.proxy.utils;

import com.spider.common.utils.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.net.InetAddress;

public class ProxyValidater {

    public static boolean checkproxy(String ip, String port) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            if (addr.isReachable(5000)) {
                //System.out.println("reached");
                return ensocketize(ip, Integer.parseInt(port));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static boolean ensocketize(String host, int port) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://blanksite.com/");
            HttpHost proxy = new HttpHost(host, port);
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);
            HttpResponse response = client.execute(get);
            HttpEntity enti = response.getEntity();
            if (response != null) {
                //System.out.println(response.getStatusLine());
                //System.out.println(response.toString());
                //System.out.println(host + ":" + port + " @@ working");

                return true;
            }
        } catch (Exception ex) {
            System.out.println("Proxy failed");
        }

        return false;
    }

    /**
     *
     * @param ip
     * @param port
     * @return
     */
    public static boolean checkByProxychecker(String ip, String port) {
        boolean isValid = false;
        String proxyChecker = "http://web.chacuo.net/netproxycheck";
        String params = String.format("data=%s&type=proxycheck&arg=p=%s_t=1_o=5", ip, port);
        String content = HttpRequestUtil.sendPost(proxyChecker, params);

        String str = StringUtils.chinaToUnicode("属于");
        if(content != null && content.indexOf(str) != -1 )
            isValid =  true;

        return isValid;
    }

    public static void main(String[] args) {

        for (int i=0; i<20; i++) {
            boolean result = ProxyValidater.checkByProxychecker("113.78.255.10", "9000");

            System.out.println("Proxy validate result: " + result);
        }
    }
}
