package com.cream.sparkle.hero.net.pipeline;

import com.cream.sparkle.hero.net.component.LinkContainer;
import com.cream.sparkle.hero.net.component.MsgDispatcher;
import com.cream.sparkle.hero.net.component.ThreadRouter;
import com.cream.sparkle.hero.net.constants.DisconnectReason;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketException;

@Slf4j
@ChannelHandler.Sharable
@Component
public class WebSocketMsgHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    private final LinkContainer linkContainer;
    private final MsgDispatcher msgDispatcher;

    @Autowired
    public WebSocketMsgHandler(LinkContainer linkContainer, MsgDispatcher msgDispatcher) {
        this.linkContainer = linkContainer;
        this.msgDispatcher = msgDispatcher;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String reqMsgJsonStr = msg.text();
        JsonObject jsonObject;
        try {
            jsonObject = JsonParser.parseString(reqMsgJsonStr).getAsJsonObject();
        } catch (Exception e) {
            log.error("收到请求消息后,json解析异常; json:{}", reqMsgJsonStr, e);
            return;
        }
        long uid = TokenValidator.getUIdAfterLogin(ctx.channel());
        this.msgDispatcher.dispatchReqMsg(uid, jsonObject);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端链接:{}", ctx.channel().id().asShortText());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开: {}", ctx.channel().id().asShortText());
        long uid = TokenValidator.getUIdAfterLogin(ctx.channel());
        try {
            ThreadRouter.routing2User(uid, () -> this.linkContainer.handleDisconnect(ctx.channel(), DisconnectReason.NetDisconnect))
                    .get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.channelInactive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof SocketException socketException && socketException.getMessage().equals("Connection reset")) {
            // todo 应该可以忽略
            log.debug("客户端异常断开连接;比如直接杀死进程");
        } else {
            log.error("Exception caught: ", cause);
        }
        ctx.close();
    }
}
