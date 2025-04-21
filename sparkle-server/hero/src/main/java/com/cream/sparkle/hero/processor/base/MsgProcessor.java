package com.cream.sparkle.hero.processor.base;

import com.cream.sparkle.hero.net.constants.ReqMsgType;
import lombok.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class MsgProcessor<T> {
    
    public void process(long rid, @NonNull T data) {
    }

    public void process(long rid) {
    }

    public abstract ReqMsgType matchType();

    /**
     * 获取子类继承时指定的T
     */
    public final Type getDataType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        return actualTypeArguments[0];
    }
}
