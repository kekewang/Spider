package com.spider.component;


import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.springframework.stereotype.Component;

import java.net.HttpCookie;
import java.util.*;
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
            if(cookies.containsKey(cookie.getName())){
                cookies.put(cookie.getName(), cookie);
            }
            else {
                cookies.remove(cookie.getName());
            }
        }
    }

    @Override
    public List<Cookie> getCookies() {
        List<Cookie> cookies = new ArrayList<Cookie>();
        Iterator iterator = this.cookies.keySet().iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            Cookie cookie = this.cookies.get(key);

            cookies.add(cookie);
        }
        return cookies;
    }

    @Override
    public boolean clearExpired(Date date) {
        return false;
    }

    @Override
    public void clear() {

    }
}
