package com.cream.sparkle.hero.net.component;

import com.cream.sparkle.common.error.Err;
import com.cream.sparkle.common.utils.json.JsonCustomLongCodecUtil;
import com.cream.sparkle.hero.net.constants.ReqMsgType;
import com.cream.sparkle.hero.processor.base.MsgProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        MsgProcessor<?> reqMsgProcessor;
        Object payloadData;
        try {
            msgType = getMsgType(reqMsg);
            reqMsgProcessor = getMsgProcessor(msgType);
            payloadData = getPayloadData(reqMsg, reqMsgProcessor);
        } catch (Err e) {
            log.error("解析消息异常", e);
            return;
        }
        // 对于登录前id设置为uid，对于登录后id设置为rid
        long idParam = msgType == ReqMsgType.LoginRole.value ? uid : this.linkContainer.getRidByUid(uid);
        @SuppressWarnings("unchecked")
        MsgProcessor<Object> processor = (MsgProcessor<Object>) reqMsgProcessor;
        Runnable processTask;
        if (payloadData == null) {
            processTask = () -> processor.process(idParam);
        } else {
            processTask = () -> processor.process(idParam, payloadData);
        }
    }

    private int getMsgType(JsonObject reqMsg) throws Err {
        try {
            return reqMsg.get(MsgTypeJsonKey).getAsInt();
        } catch (Exception e) {
            throw new Err("请求消息类型格式错误, msgType:" + reqMsg.get(MsgTypeJsonKey), e);
        }
    }

    private MsgProcessor<?> getMsgProcessor(int msgType) throws Err {
        MsgProcessor<?> reqMsgProcessor = this.reqMsgType2Processor.get(msgType);
        if (reqMsgProcessor == null) {
            throw new Err("未找到对应类型的请求消息处理器,msgType:" + msgType);
        }
        return reqMsgProcessor;
    }

    /**
     * 获取荷载的数据
     */
    private Object getPayloadData(JsonObject reqMsg, MsgProcessor<?> reqMsgProcessor) throws Err {
        // 获取消息处理指定的荷载类型
        Type needDataType = reqMsgProcessor.getDataType();
        // 获取实际发送的荷载数据
        JsonElement dataJsonElement = reqMsg.get(DataJsonKey);
        try {
            if (needDataType == Void.class) { //指定无需荷载数据
                // 校验
                if (!dataJsonElement.isJsonNull()) {
                    throw new Err("消息处理指定无需荷载数据, 实际请求荷载不为空");
                }
                return null;
            } else if (needDataType == Boolean.class) {
                return dataJsonElement.getAsBoolean();
            } else if (needDataType == Integer.class) {
                return dataJsonElement.getAsInt();
            } else if (needDataType == String.class) {
                return dataJsonElement.getAsString();
            } else {
                // 这里包括了needDataType是Long,因为Long有特殊转换,它是以{_isLong:true,_longStr:"xxx"}的形式传递过来的
                return JsonCustomLongCodecUtil.fromJsonElement(dataJsonElement.getAsJsonObject(), needDataType);
            }
        } catch (Exception e) {
            throw new Err("获取荷载数据异常", e);
        }
    }
}
