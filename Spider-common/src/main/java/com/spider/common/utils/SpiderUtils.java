package com.spider.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpiderUtils {

    public static void writeFile(String fileName, String path, byte[] byteContent){
        OutputStream opStream = null;
        try {
            File fp = new File(path);
            if (!fp.exists()) {
                fp.mkdirs();
            }

            File myFile = new File(path + File.separator + fileName);
            // check if file exist, otherwise create the file before writing
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            opStream = new FileOutputStream(myFile);
            opStream.write(byteContent);
            opStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(opStream != null) opStream.close();
            } catch(Exception ex){

            }
        }
    }

    public static String getMatcher(String regex, String source) {
        String result = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            result = matcher.group(1);//只取第一组
        }
        return result;
    }
}
