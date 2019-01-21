package com.spider.service;

public interface Refreshable {

    boolean isNeedRefresh();

    int getIntervalSeconds();

    void refresh();

    String getName();
}
