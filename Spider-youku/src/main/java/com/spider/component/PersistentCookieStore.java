package com.spider.component;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.util.Date;
import java.util.List;

public class PersistentCookieStore implements CookieStore {
    @Override
    public void addCookie(Cookie cookie) {

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
