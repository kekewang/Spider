package com.spider.spider;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class GithubSpider extends Spider {

    private String keyword;

    public GithubSpider(PageProcessor pageProcessor) {
        super(pageProcessor);
    }

    public GithubSpider keyword(String keyword){
        this.keyword = keyword;

        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
