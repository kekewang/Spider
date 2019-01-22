package com.spider.component;

import com.spider.processer.YoukuProcesser;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProxyComponent {

    public List<Proxy> proxyList = new ArrayList();

    public ProxyProvider getSimpleProxyProvider() {

        BufferedReader proxyIpReader = new BufferedReader(new InputStreamReader(YoukuProcesser.class.getResourceAsStream("/config/proxyip.txt")));

        String[] ip = new String[4];
        try {
            String socket = null;
            while ((socket = proxyIpReader.readLine()) != null) {
                String[] ipandport = socket.split(":");

                if (ipandport.length == 2) {
                    Proxy proxy = new Proxy(ipandport[0], Integer.parseInt(ipandport[1]));
                    proxyList.add(proxy);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        SimpleProxyProvider proxyProvider = new SimpleProxyProvider(proxyList);

        return proxyProvider;
    }
}
