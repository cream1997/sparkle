package com.cream.sparkle.hero.net.pipeline;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cream.sparkle.hero.net.component.LinkContainer;
import com.cream.sparkle.hero.net.component.MsgDispatcher;
import com.cream.sparkle.hero.net.constants.DisconnectReason;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        log.info("Received message: {}", msg.text());
        JSONObject parseMsg = (JSONObject) JSON.parse(msg.text());
        long uid = TokenValidator.getUIdAfterLogin(ctx.channel());
        this.msgDispatcher.dispatchReqMsg(uid, parseMsg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端链接:{}", ctx.channel().id().asShortText());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开: {}", ctx.channel().id().asShortText());
        /*
         * 这里要判断一下，因为顶号的断开不在这里执行(断开不能统一在这里执行，会有线程问题)，这里不能重复执行；
         * 因为这个方法的执行是由netty线程执行的,如果重复执行，有可能导致顶号的新号刚登陆的地图被退出了，总之,判断一下最大程度规避风险
         */
        if (linkContainer.containsChannel(ctx.channel())) {
            this.linkContainer.handleDisconnect(ctx.channel(), DisconnectReason.NetDisconnect);
        }
        super.channelInactive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception caught: ", cause);
        ctx.close();
    }
}
