package com.cream.sparkle.utils;

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
}
