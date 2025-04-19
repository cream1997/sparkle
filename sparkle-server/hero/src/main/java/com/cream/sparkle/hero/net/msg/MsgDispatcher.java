package com.cream.sparkle.hero.net.msg;

import com.cream.sparkle.hero.manager.MapManager;
import com.cream.sparkle.hero.net.constants.DisconnectReason;
import com.cream.sparkle.hero.net.handler.TokenValidator;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MsgDispatcher {

    private final ConcurrentHashMap<Long, Channel> uid2Channel = new ConcurrentHashMap<>();

    private final MapManager mapManager;

    @Autowired
    public MsgDispatcher(MapManager mapManager) {
        this.mapManager = mapManager;
    }

    public void putChannel(Channel channel) {
        long uid = TokenValidator.getUIdAfterLogin(channel);
        Channel oldChannel = this.uid2Channel.get(uid);
        if (oldChannel != null) {
            log.info("顶号，uid:{}", uid);
            /*
             * 旧连接断开处理;不能依赖于oldChannel.close()触发的channelInactive去处理断开,因为它是异步的;
             * 即使像这样oldChannel.close().sync()调用也不能阻止这种异步;所以在断开连接前主动调用
             */
            handleDisconnect(oldChannel, DisconnectReason.DingHao);
            // 断开旧连接
            oldChannel.close();
        }
        this.uid2Channel.put(uid, channel);
    }

    /**
     * @param channel 要删除的channel
     */
    public void removeChannel(Channel channel) {
        long uid = TokenValidator.getUIdAfterLogin(channel);
        /*
         * 之所以要这样判断一下，是因为在顶号时，oldChannel.close()会出发一个异步的channelInactive，里面有个removeChannel的操作
         * 而在oldChannel.close()后还有个put new channel操作,它会先于异步的removeChannel，导致先put后remove
         */
        if (channel == this.uid2Channel.get(uid)) {
            this.uid2Channel.remove(uid);
        }
    }

    public boolean containsChannel(Channel channel) {
        long uid = TokenValidator.getUIdAfterLogin(channel);
        return channel == this.uid2Channel.get(uid);
    }

    /**
     * 处理断开
     */
    public void handleDisconnect(Channel channel, DisconnectReason reason) {
        this.mapManager.exitMap(TokenValidator.getUIdAfterLogin(channel));
        removeChannel(channel);
    }
}
