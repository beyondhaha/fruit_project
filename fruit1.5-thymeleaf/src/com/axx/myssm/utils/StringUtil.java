package com.axx.myssm.utils;

public class StringUtil {
    //判断字符串是否为null
    public static boolean isEmpty(String str) {

        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
