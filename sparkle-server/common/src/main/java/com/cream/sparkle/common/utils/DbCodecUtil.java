package com.cream.sparkle.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;

import java.nio.charset.StandardCharsets;

public class DbCodecUtil {

    public static byte[] format(Object obj) {
        return JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8);
    }

    public static <T> T parse(byte[] data, Class<T> tClass) {
        String jsonStr = new String(data, StandardCharsets.UTF_8);
        return JSON.parseObject(jsonStr, tClass, JSONReader.Feature.FieldBased);
    }
}
