package com.spider.proxy.utils;

import com.spider.common.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

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

    public static boolean checkProxy(String ip, String port) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL("https://movie.douban.com/subject/30149826/");

            // 创建代理服务器
            InetSocketAddress addr = new InetSocketAddress(ip, Integer.valueOf(port));
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); //http 代理

            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection(proxy);

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            // 建立实际的连接
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "gbk"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            Document doc = Jsoup.parse(new String(result.getBytes("ISO-8859-1")));
            String title = doc.select("span[property=v:itemreviewed]").text();
            if (title.contains("Fauve"))
                return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {

            }
        }
        return false;
    }

    public static void main(String[] args) {

        for (int i=0; i<20; i++) {
            boolean result = ProxyValidater.checkByProxychecker("113.78.255.10", "9000");

            System.out.println("Proxy validate result: " + result);
        }
    }
}
