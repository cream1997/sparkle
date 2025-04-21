package com.cream.sparkle.hero.net.constants;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public enum ResMsgType {
    LoginRole(0),
    ;

    public final int value;

    ResMsgType(int value) {
        this.value = value;
    }

    private static final Map<Integer, ResMsgType> value2enum = new ConcurrentHashMap<>();

    static {
        for (ResMsgType typeEnum : ResMsgType.values()) {
            value2enum.put(typeEnum.value, typeEnum);
        }
    }

    public static ResMsgType getByValue(int value) {
        ResMsgType resMsgType = value2enum.get(value);
        if (resMsgType == null) {
            log.error("获取响应消息类型为空, value:{}", value);
        }
        return resMsgType;
    }
}
