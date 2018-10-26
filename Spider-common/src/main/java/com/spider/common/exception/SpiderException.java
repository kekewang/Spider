package com.spider.common.exception;

import java.util.HashMap;

public class SpiderException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    public final static String BUSINESS_EXCEPTION_CODE = "SPIDER0000";

    private HashMap<String, Object> extendData;

    public SpiderException() {
        super("系统异常");
    }

    public SpiderException(String message) {
        super(message);
    }

    public HashMap<String, Object> getExtendData() {
        return extendData;
    }

    public void setExtendData(HashMap<String, Object> extendData) {
        this.extendData = extendData;
    }

}