package com.spider.component;


import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.springframework.stereotype.Component;

import java.net.HttpCookie;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PersistentCookieStore implements CookieStore {
    private final Map<String, Cookie> cookies;

    public PersistentCookieStore() {
        cookies = new HashMap<String, Cookie>();
    }

    @Override
    public void addCookie(Cookie cookie) {
        if (cookie.isExpired(new Date())){
            //cookies.containsKey(cookie.getName())
        }
    }

    @Override
    public List<Cookie> getCookies() {
        return null;
    }

    @Override
    public boolean clearExpired(Date date) {
        return false;
    }

    @Override
    public void clear() {

    }
}
