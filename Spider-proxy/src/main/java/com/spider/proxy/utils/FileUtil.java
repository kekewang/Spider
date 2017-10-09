package com.spider.proxy.utils;

import com.spider.proxy.processer.XiciProcesser;

import java.io.*;

/**
 * Created by wangke on 2017/10/9.
 */
public class FileUtil {

    public synchronized static void saveProxy(String file, String proxy){
        String path = XiciProcesser.class.getResource(file).getPath();
        BufferedWriter proxyIpWriter = null;
        try {
            FileOutputStream outputFile = new FileOutputStream(path,true);
            OutputStreamWriter writer = new OutputStreamWriter(outputFile);
            proxyIpWriter = new BufferedWriter(writer);
            proxyIpWriter.write(proxy);
            proxyIpWriter.newLine();
            proxyIpWriter.flush();
            writer.close();
            proxyIpWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
