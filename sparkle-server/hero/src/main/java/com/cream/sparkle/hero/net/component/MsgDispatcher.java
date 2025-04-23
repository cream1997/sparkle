package com.cream.sparkle.hero.net.component;

import com.cream.sparkle.common.error.Err;
import com.cream.sparkle.common.utils.json.JsonCustomLongCodecUtil;
import com.cream.sparkle.hero.net.constants.ReqMsgType;
import com.cream.sparkle.hero.processor.base.MsgProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MsgDispatcher {

    private static final String MsgTypeJsonKey = "msgType";
    private static final String DataJsonKey = "data";

    private final ConcurrentHashMap<Integer, MsgProcessor<?>> reqMsgType2Processor = new ConcurrentHashMap<>();
    private final LinkContainer linkContainer;

    @Autowired
    public MsgDispatcher(LinkContainer linkContainer) {
        this.linkContainer = linkContainer;
    }

    public void registryReqMsgProcessor(MsgProcessor<?> reqMsgProcessor) {
        reqMsgType2Processor.put(reqMsgProcessor.matchType().value, reqMsgProcessor);
        log.info("注册请求消息处理器:{}", reqMsgProcessor);
    }

    public void dispatchReqMsg(long uid, JsonObject reqMsg) {
        int msgType;
        try {
            msgType = reqMsg.get(MsgTypeJsonKey).getAsInt();
        } catch (Exception e) {
            log.error("请求消息类型格式错误, msgType:{}", reqMsg.get(MsgTypeJsonKey), e);
            return;
        }
        MsgProcessor<?> reqMsgProcessor = this.reqMsgType2Processor.get(msgType);
        if (reqMsgProcessor == null) {
            log.error("未找到对应类型的请求消息处理器,msgType:{}", msgType);
            return;
        }
        // 对于登录前id设置为uid，对于登录后id设置为rid
        long id;
        if (msgType == ReqMsgType.LoginRole.value) {
            id = uid;
        } else {
            id = this.linkContainer.getRidByUid(uid);
        }
        JsonElement dataJsonElement = reqMsg.get(DataJsonKey);
        if (dataJsonElement.isJsonNull()) {
            reqMsgProcessor.process(id);
            return;
        }
        Object data;
        if (dataJsonElement.isJsonObject()) {
            //数据是非基本类型(String、Long...)需要转换
            Type dataType = reqMsgProcessor.getDataType();
            try {
                data = JsonCustomLongCodecUtil.fromJsonElement(dataJsonElement.getAsJsonObject(), dataType);
            } catch (Exception e) {
                log.error("请求消息数据转换异常", e);
                return;
            }
        } else {
            try {
                data = primitiveConvert(dataJsonElement.getAsJsonPrimitive());
            } catch (Err e) {
                log.error("类型转换异常", e);
                return;
            }
        }
        @SuppressWarnings("unchecked")
        MsgProcessor<Object> processor = (MsgProcessor<Object>) reqMsgProcessor;
        try {
            processor.process(id, data);
        } catch (Exception e) {
            log.error("消息处理执行异常", e);
        }
    }

    private Object primitiveConvert(JsonPrimitive jsonPrimitive) throws Err {
        if (jsonPrimitive.isBoolean()) {
            return jsonPrimitive.getAsBoolean();
        } else if (jsonPrimitive.isNumber()) {
            long number = jsonPrimitive.getAsLong();
            if (number >= Integer.MIN_VALUE && number <= Integer.MAX_VALUE) {
                return (int) number;
            } else {
                return number;
            }
        } else if (jsonPrimitive.isString()) {
            return jsonPrimitive.getAsString();
        } else {
            throw new Err("暂不支持的类型：" + jsonPrimitive);
        }
    }
}
