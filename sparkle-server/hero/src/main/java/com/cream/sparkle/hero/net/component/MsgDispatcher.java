package com.cream.sparkle.hero.net.component;

import com.alibaba.fastjson2.JSONObject;
import com.cream.sparkle.hero.net.constants.ReqMsgType;
import com.cream.sparkle.hero.processor.base.MsgProcessor;
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

    public void dispatchReqMsg(long uid, JSONObject reqMsg) {
        Integer msgType;
        try {
            msgType = (Integer) reqMsg.get(MsgTypeJsonKey);
        } catch (ClassCastException e) {
            log.error("请求消息类型格式错误, msgType:{}", reqMsg.get(MsgTypeJsonKey));
            return;
        }
        MsgProcessor<?> reqMsgProcessor = this.reqMsgType2Processor.get(msgType);
        if (reqMsgProcessor == null) {
            log.error("未找到对应类型的请求消息处理器,msgType:{}", msgType);
            return;
        }
        long rid = this.linkContainer.getRidByUid(uid);
        if (msgType != ReqMsgType.LoginRole.value && rid == 0) {
            log.error("rid获取失败,msgType:{}", ReqMsgType.getByValue(msgType));
            return;
        }
        Object data = reqMsg.get(DataJsonKey);
        if (data == null) {
            reqMsgProcessor.process(rid);
            return;
        }
        if (data instanceof JSONObject jsonObject) {
            //数据是非基本类型(String、Long...)需要转换
            Type dataType = reqMsgProcessor.getDataType();
            try {
                data = jsonObject.to(dataType);
            } catch (Exception e) {
                log.error("请求消息数据转换异常", e);
            }
        }
        @SuppressWarnings("unchecked")
        MsgProcessor<Object> processor = (MsgProcessor<Object>) reqMsgProcessor;
        processor.process(rid, data);
    }
}
