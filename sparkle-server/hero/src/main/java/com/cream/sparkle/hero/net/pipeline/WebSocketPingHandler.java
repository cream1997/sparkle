package com.cream.sparkle.hero.net.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 打印ping，不做其他处理；WebSocketServerProtocolHandler默认会自动处理ping返回pong,同时过滤ping消息传递,
 * 所以这个类在pipeline中要放到WebSocketServerProtocolHandler之前
 * 另外由于Idle事件放到这个类处理，所以这个类要放到IdleStateHandler之后，这样他才能感知Idle事件(可能也有其他方式能控制，不过这样做很简单)
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketPingHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof PingWebSocketFrame) {
            log.debug("收到ping消息，uid:{}", TokenValidator.getUIdAfterLogin(ctx.channel()));
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 关闭空闲连接
            log.debug("连接空闲: uid:{}", TokenValidator.getUIdAfterLogin(ctx.channel()));
            ctx.close();
        }
    }
}
