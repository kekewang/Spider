package com.snm.utils;

/**
 * Created by wangke on 2017/9/28.
 */
public class StringUtils {
    /**
     * 是否为空
     * @param str 字符串
     * @return true 空 false 非空
     */
    public static Boolean isEmpty(String str) {
        if(str == null || str.equals("")) {
            return true;
        }

        return false;
    }
}
