package com.cream.sparkle.hero.net;

import com.cream.sparkle.hero.net.pipeline.TokenValidator;
import com.cream.sparkle.hero.net.pipeline.WebSocketMsgHandler;
import com.cream.sparkle.hero.net.pipeline.WebSocketPingHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GameServer {

    @Value("${hero.socketPort:8899}")
    private int socketPort;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private static final String WEBSOCKET_PATH = "/ws";

    private final TokenValidator tokenValidator;
    private final WebSocketMsgHandler webSocketMsgHandler;
    private final WebSocketPingHandler webSocketPingHandler;

    @Autowired
    public GameServer(TokenValidator tokenValidator, WebSocketMsgHandler webSocketMsgHandler, WebSocketPingHandler webSocketPingHandler) {
        this.tokenValidator = tokenValidator;
        this.webSocketMsgHandler = webSocketMsgHandler;
        this.webSocketPingHandler = webSocketPingHandler;
    }


    @PostConstruct
    public void start() throws InterruptedException {
        // fixme 线程数后续根据性能测试修改
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(2);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                //似乎只需要在logback中配置就好，这里好像不需要加
                //.handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(65535))
                                .addLast(tokenValidator) //在握手之前验证token
                                // IdleStateHandler和webSocketPingHandler要放到WebSocketServerProtocolHandler之前，否则它们感知不到ping
                                // 因为WebSocketServerProtocolHandler会默认处理且过滤Ping消息
                                // fixme 空闲时间控制待定
                                .addLast(new IdleStateHandler(15, 15, 15))
                                .addLast(webSocketPingHandler)
                                //.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true, 64 * 1024, true, true, 10000))
                                .addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, true))
                                .addLast(webSocketMsgHandler);
                    }
                })
                // TCP参数设置
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.bind(socketPort).sync();
        log.info("Game Server started on port:{}, webSocketPath:{}", socketPort, WEBSOCKET_PATH);
    }

    @PreDestroy
    public void shutdown() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        log.info("Game Server stopped");
    }

}
