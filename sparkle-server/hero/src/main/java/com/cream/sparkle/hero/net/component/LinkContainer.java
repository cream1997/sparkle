package com.cream.sparkle.hero.net.component;

import com.cream.sparkle.hero.manager.LoginManager;
import com.cream.sparkle.hero.net.constants.DisconnectReason;
import com.cream.sparkle.hero.net.pipeline.TokenValidator;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class LinkContainer {

    private final ConcurrentHashMap<Long, Channel> Uid2Channel = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Long> Uid2Rid = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Long> Rid2Uid = new ConcurrentHashMap<>();

    private final LoginManager loginManager;

    @Autowired
    public LinkContainer(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public void handleNewLink(Channel channel) {
        long uid = TokenValidator.getUIdAfterLogin(channel);
        Channel oldChannel = Uid2Channel.get(uid);
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
        Uid2Channel.put(uid, channel);
    }

    /**
     * 处理断开
     */
    public void handleDisconnect(Channel channel, DisconnectReason reason) {
        if (!Uid2Channel.containsValue(channel)) {
            return;
        }
        long uid = TokenValidator.getUIdAfterLogin(channel);
        long rid = getRidByUidMayNull(uid);
        if (rid != 0) {
            this.loginManager.logoutRole(uid, rid);
            removeRid(rid);
        }
        Uid2Channel.remove(uid);
    }

    public long getUidByRid(long rid) {
        Long uid = Rid2Uid.getOrDefault(rid, 0L);
        if (uid == 0) {
            log.error("获取uid为空, rid:{}", rid);
        }
        return uid;
    }

    public void setRid(long uid, long rid) {
        Uid2Rid.put(uid, rid);
        Rid2Uid.put(rid, uid);
    }

    public long getRidByUid(long uid) {
        Long rid = Uid2Rid.getOrDefault(uid, 0L);
        if (rid == 0) {
            log.error("获取rid为空, uid:{}", uid);
        }
        return rid;
    }

    /**
     * 这个方法允许返回为空(即0)，也就是为空不会打印错误日志
     * 因为在有些时机获取rid是允许为空的，比如登录角色前
     */
    public long getRidByUidMayNull(long uid) {
        return Uid2Rid.getOrDefault(uid, 0L);
    }

    /**
     * 退出当前角色或者退出整个账号时remove
     */
    public void removeRid(long rid) {
        Long uid = Rid2Uid.get(rid);
        Objects.requireNonNull(uid);
        Rid2Uid.remove(rid);
        Uid2Rid.remove(uid);
    }

    public Channel getChannelByUid(long uid) {
        return Uid2Channel.get(uid);
    }
}
