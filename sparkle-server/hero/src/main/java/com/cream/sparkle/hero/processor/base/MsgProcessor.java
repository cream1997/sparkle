package com.cream.sparkle.hero.processor.base;

import com.cream.sparkle.hero.net.constants.ReqMsgType;
import lombok.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class MsgProcessor<T> {

    /**
     * @param id 对于登录角色前的请求会传uid,对于登录后的会传rid
     */
    public void process(long id, @NonNull T data) {
    }

    /**
     * @param id 对于登录角色前的请求会传uid,对于登录后的会传rid
     */
    public void process(long id) {
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
