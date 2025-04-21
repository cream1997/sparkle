package com.cream.sparkle.hero.net.constants;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public enum ReqMsgType {
    LoginRole(0);

    ReqMsgType(int value) {
        this.value = value;
    }

    public final int value;

    private static final Map<Integer, ReqMsgType> value2enum = new ConcurrentHashMap<>();

    static {
        for (ReqMsgType typeEnum : ReqMsgType.values()) {
            value2enum.put(typeEnum.value, typeEnum);
        }
    }

    public static ReqMsgType getByValue(int value) {
        ReqMsgType reqMsgType = value2enum.get(value);
        if (reqMsgType == null) {
            log.error("获取请求消息类型为空, value:{}", value);
        }
        return reqMsgType;
    }
}
