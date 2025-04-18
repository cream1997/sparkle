package com.cream.sparkle.hero.net.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@ChannelHandler.Sharable
@Component
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

//    private final MsgDispatcher msgDispatcher;
//    private final MapManager mapManager;

//    @Autowired
//    public WebSocketHandler(MsgDispatcher msgDispatcher, MapManager mapManager) {
//        this.msgDispatcher = msgDispatcher;
//        this.mapManager = mapManager;
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("Received message: {}", msg.text());
        JSONObject parseMsg = (JSONObject) JSON.parse(msg.text());
        long uid = TokenValidator.getUIdAfterLogin(ctx.channel());
//        this.msgDispatcher.dispatch(id, parseMsg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端链接:{}", ctx.channel().id().asShortText());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开: {}", ctx.channel().id().asShortText());
//        mapManager.logout(TokenValidator.getIdAfterLogin(ctx.channel()));
//        this.msgDispatcher.removeChannel(ctx.channel());
        super.channelInactive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception caught: ", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 关闭空闲连接
            log.info("连接空闲: uid:{}", TokenValidator.getUIdAfterLogin(ctx.channel()));
            // todo 将来加入心跳机制后方法注释
//            ctx.close();
        }
    }
}
