package com.spider.proxy.utils;

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

    public static void main(String[] args) {

        boolean result = ProxyValidater.checkproxy("125.118.65.122", "9999");

        System.out.println("Proxy validate result: " + result);
    }
}
