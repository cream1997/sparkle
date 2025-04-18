package com.cream.sparkle.hero.net.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketPingHandler extends ChannelInboundHandlerAdapter {

    /**
     * 这个类目前只处理ping消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof PingWebSocketFrame) {
            log.info("收到ping消息：{}", msg);
            ctx.writeAndFlush(new PingWebSocketFrame());
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 关闭空闲连接
            log.info("连接空闲: uid:{}", TokenValidator.getUIdAfterLogin(ctx.channel()));
            // todo 心跳机制完善后取消注释
            // ctx.close();
        }
    }
}
