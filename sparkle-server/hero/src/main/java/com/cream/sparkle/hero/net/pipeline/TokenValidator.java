package com.cream.sparkle.hero.net.pipeline;

import com.cream.sparkle.common.error.Err;
import com.cream.sparkle.common.utils.JwtUtil;
import com.cream.sparkle.common.utils.Nulls;
import com.cream.sparkle.hero.net.component.LinkContainer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@ChannelHandler.Sharable
@Component
public class TokenValidator extends ChannelInboundHandlerAdapter {

    private static final AttributeKey<Long> UID_KEY = AttributeKey.newInstance("uidKey");

    private final LinkContainer linkContainer;

    @Autowired
    public TokenValidator(LinkContainer linkContainer) {
        this.linkContainer = linkContainer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (isVerified(ctx.channel())) {
            ctx.fireChannelRead(msg);
            return;
        }
        FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
        QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
        Map<String, List<String>> parameters = decoder.parameters();
        List<String> tokens = parameters.get("token");
        if (Nulls.isEmpty(tokens)) {
            log.error("建立ws连接时未携带token信息");
            ctx.close();
            return;
        }
        String token = tokens.getFirst();
        JwtUtil.IdAndQx idAndQx;
        try {
            idAndQx = JwtUtil.extractIdAndQx(token);
        } catch (Err e) {
            log.error("token验证不合法，拒绝连接");
            ctx.close();
            return;
        }
        // 验证成功，存入id
        ctx.channel().attr(UID_KEY).set(idAndQx.id);
        this.linkContainer.handleNewLink(ctx.channel());
        ctx.fireChannelRead(msg);
    }

    private boolean isVerified(Channel channel) {
        return channel.attr(UID_KEY).get() != null;
    }

    public static long getUIdAfterLogin(Channel channel) {
        Attribute<Long> attr = channel.attr(UID_KEY);
        if (attr == null || attr.get() == null) {
            log.error("获取id为空，channel:{}", channel);
            return 0;
        }
        return attr.get();
    }
}
