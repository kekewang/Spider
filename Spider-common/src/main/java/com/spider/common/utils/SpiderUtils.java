package com.spider.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class SpiderUtils {

    public static void writeFile(String fileName, String path, byte[] byteContent){
        OutputStream opStream = null;
        try {
            File myFile = new File(path + fileName);
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
}
