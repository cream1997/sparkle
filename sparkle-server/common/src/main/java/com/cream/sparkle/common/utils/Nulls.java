package com.cream.sparkle.common.utils;

import java.util.Collection;

/**
 * 判空工具类
 */
public class Nulls {
    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null) {
            return true;
        }
        return collection.isEmpty();
    }

    public static boolean anyEmpty(Collection<?>... collections) {
        if (collections == null) {
            return true;
        }
        for (Collection<?> collection : collections) {
            if (isEmpty(collection)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean anyBlank(String... allStr) {
        if (allStr == null || allStr.length == 0) {
            return true;
        }
        for (String str : allStr) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }
}
