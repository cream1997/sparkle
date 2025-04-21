package com.cream.sparkle.hero.context;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.cream.sparkle.hero.manager.MapManager;
import com.cream.sparkle.hero.net.component.LinkContainer;
import com.cream.sparkle.hero.net.msg.BaseRes;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Context {

    private static LinkContainer linkContainer;
    private static MapManager mapManager;

    @Autowired
    public Context(LinkContainer linkContainer, MapManager mapManager) {
        Context.linkContainer = linkContainer;
        Context.mapManager = mapManager;
    }


    public static void sendMsg(long rid, BaseRes resMsg) {
        Channel channel = linkContainer.getChannel(rid);
        if (channel != null) {
            DataWrapper dataWrapper = new DataWrapper(resMsg.msgType().value, resMsg);
            String jsonString = JSON.toJSONString(dataWrapper, JSONWriter.Feature.FieldBased);
            channel.writeAndFlush(new TextWebSocketFrame(jsonString));
        }
    }

    private static class DataWrapper {
        private final int msgType;
        private final BaseRes data;

        private DataWrapper(int msgType, BaseRes data) {
            this.msgType = msgType;
            this.data = data;
        }
    }
}
