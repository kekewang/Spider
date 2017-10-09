package com.spider.proxy.enums;

/**
 * Created by wangke on 2017/10/9.
 */
public enum ProxyType {
    HTTP("HTTP"),

    HTTPS("HTTPS");

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    private ProxyType(String type){
        this.type = type;
    }
}