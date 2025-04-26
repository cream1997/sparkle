package com.cream.sparkle.hero.manager;

import com.cream.sparkle.common.error.RunErr;
import com.cream.sparkle.hero.context.Context;
import com.cream.sparkle.hero.context.ExecutorsUtil;
import com.cream.sparkle.hero.context.RoleHeart;
import com.cream.sparkle.hero.game.role.Role;
import com.cream.sparkle.hero.net.component.LinkContainer;
import com.cream.sparkle.hero.net.component.ThreadRouter;
import com.cream.sparkle.hero.net.msg.res.LoginRoleRes;
import com.cream.sparkle.hero.tools.RoleDbTool;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RoleManager {

    private final LinkContainer linkContainer;
    private final RoleDbTool roleDbTool;
    private final MapManager mapManager;
    private final ConcurrentHashMap<Long, RoleHeart> rid2RoleHeart = new ConcurrentHashMap<>();

    @Autowired
    public RoleManager(LinkContainer linkContainer, RoleDbTool roleDbTool, MapManager mapManager) {
        this.linkContainer = linkContainer;
        this.roleDbTool = roleDbTool;
        this.mapManager = mapManager;
        ExecutorsUtil.runFixedDelay(() -> rid2RoleHeart.forEach((rid, roleHeart) -> {
            ThreadRouter.routing2Role(rid, roleHeart::heartPerSecond);
        }), 0, 1, TimeUnit.SECONDS);
    }

    private Role getRole(long rid) {
        Role role = this.roleDbTool.selectRole(rid);
        Objects.requireNonNull(role);
        return role;
    }


    public void enterRole(long uid, long rid) {
        Channel channel = linkContainer.getChannelByUid(uid);
        if (channel == null) {
            log.error("执行登录前已断开连接; uid:{}, rid:{}", uid, rid);
            return;
        }
        Role role = getRole(rid);
        if (role.basic.uid != rid) {
            throw new RunErr("非法登录,uid:" + uid + ", rid:" + rid);
        }
        linkContainer.setRid(uid, rid);
        // todo 返回登录需要返回的信息
        long mapId = role.basic.getMapId();
        Future<Void> mapThreadLoginFuture = ThreadRouter.routing2MapByMapId(mapId, () -> {
            // todo 登录地图
            mapManager.loginMap(role);
        });
        try {
            mapThreadLoginFuture.get();
        } catch (Exception e) {
            log.error("登录地图异常", e);
            linkContainer.removeRid(rid);
        }
        role.basic.setMapId(mapId);
        LoginRoleRes loginRoleRes = new LoginRoleRes(role, 0, "", 0, 0);
        Context.sendMsgByUid(role.getRid(), loginRoleRes);

        // 登录成功后，开启玩家心跳
        registerRoleHeart(role);
    }

    public void exitRole(long rid) {
        Role role = getRole(rid);
        Objects.requireNonNull(role);
        Future<Void> mapThreadFuture = ThreadRouter.routing2MapByMapId(role.basic.getMapId(), () -> {
            // todo 退出地图
        });
        try {
            mapThreadFuture.get();
        } catch (Exception e) {
            log.error("退出地图异常", e);
        } finally {
            removeRoleHeart(rid);
            linkContainer.removeRid(rid);
        }
    }

    private void registerRoleHeart(Role role) {
        RoleHeart roleHeart = rid2RoleHeart.putIfAbsent(role.getRid(), new RoleHeart(role));
        if (roleHeart != null) {
            log.error("玩家心跳已存在; rid:{}", role.getRid());
        }
    }

    private void removeRoleHeart(long rid) {
        RoleHeart remove = rid2RoleHeart.remove(rid);
        if (remove == null) {
            log.error("删除玩家心跳时,心跳不存在;rid:{}", rid);
        }
    }
}
