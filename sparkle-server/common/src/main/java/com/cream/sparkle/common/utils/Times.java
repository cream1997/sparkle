package com.cream.sparkle.common.utils;

/**
 * 时间工具类
 */
public class Times {

    public static long ONE_SECOND = 1000;

    public static long ONE_MINUTE = ONE_SECOND * 60;

    public static long ONE_HOUR = ONE_MINUTE * 60;

    public static long now() {
        return System.currentTimeMillis();
    }
}
