package com.cream.sparkle.common.utils.json;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * 为什么有了fastjson2还要使用Gson来实现
 * 主要是我使用fastjson在实现已定义解析规则时有一些问题搞不定,比如设置fieldBased但是不出效果，必须要结合getter方法，另外
 * 在测试使用fastjson2自定义的解析器时经常卡死，目前二者并存，hero模块使用Gson来处理ws连接的json自定义解析主要是处理long值到bigint的转换
 * 详情见com.cream.sparkle.common.utils.json.JsonCustomLongCodecUtil
 * todo 后续尝试用fastjson2实现，并将common中的Gson依赖删除，同时删除LongTypeAdapter；亦或者将使用fastjson2的部分改为Gson,然后将fastjson2删除;总之目前二者共存
 */
public class JsonCustomLongCodecUtil {

    private static final Gson gson = initGson();

    private static Gson initGson() {
        GsonBuilder builder = new GsonBuilder();
        // 注册Long类型适配器（同时处理包装类型Long和基本类型long）
        builder.registerTypeAdapter(Long.class, new LongTypeAdapter());
        builder.registerTypeAdapter(long.class, new LongTypeAdapter());
        return builder.create();
    }

    /**
     * 注意返回的是被包装的子类而非原始的T
     */
    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }
}
