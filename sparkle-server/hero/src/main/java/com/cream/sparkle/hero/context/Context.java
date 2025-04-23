package com.cream.sparkle.hero.context;

import com.cream.sparkle.common.utils.json.JsonCustomLongCodecUtil;
import com.cream.sparkle.hero.manager.MapManager;
import com.cream.sparkle.hero.net.component.LinkContainer;
import com.cream.sparkle.hero.net.msg.BaseRes;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
        long uid = linkContainer.getUidByRid(rid);
        if (uid != 0) {
            sendMsgByUid(uid, resMsg);
        }
    }

    public static void sendMsgByUid(long uid, BaseRes resMsg) {
        Channel channel = linkContainer.getChannelByUid(uid);
        Objects.requireNonNull(channel);
        DataWrapper dataWrapper = new DataWrapper(resMsg.msgType().value, resMsg);
        String jsonString = JsonCustomLongCodecUtil.toJson(dataWrapper);
        channel.writeAndFlush(new TextWebSocketFrame(jsonString));
    }

    private record DataWrapper(int msgType, BaseRes data) {
    }
}
