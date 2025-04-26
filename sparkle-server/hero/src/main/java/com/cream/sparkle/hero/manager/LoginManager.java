package com.cream.sparkle.hero.manager;

import com.cream.sparkle.hero.context.Context;
import com.cream.sparkle.hero.context.ExecutorsUtil;
import com.cream.sparkle.hero.context.RoleHeart;
import com.cream.sparkle.hero.game.logic.Role;
import com.cream.sparkle.hero.net.component.LinkContainer;
import com.cream.sparkle.hero.net.component.ThreadRouter;
import com.cream.sparkle.hero.net.msg.res.LoginRoleRes;
import com.cream.sparkle.hero.tools.RoleDbTool;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LoginManager {

    private final ConcurrentHashMap<Long, RoleHeart> rid2RoleHeart = new ConcurrentHashMap<>();
    private final LinkContainer linkContainer;
    private final RoleDbTool roleDbTool;

    @Autowired
    public LoginManager(LinkContainer linkContainer, RoleDbTool roleDbTool) {
        this.linkContainer = linkContainer;
        this.roleDbTool = roleDbTool;
        ExecutorsUtil.runFixedDelay(() -> rid2RoleHeart.forEach((rid, roleHeart) -> {
            ThreadRouter.routing2Logic(rid, roleHeart::heartPerSecond);
        }), 0, 1, TimeUnit.SECONDS);
    }

    private Role getRole(long rid) {
        return this.roleDbTool.selectRole(rid);
    }

    public void loginRole(long uid, long rid) {
        Channel channel = linkContainer.getChannelByUid(uid);
        if (channel == null) {
            log.error("执行登录前已断开连接; uid:{}, rid:{}", uid, rid);
            return;
        }
        Role role = getRole(rid);
        if (role == null) {
            return;
        }
        linkContainer.setRid(uid, rid);
        Future<Void> loginThreadLoginFuture = ThreadRouter.routing2Logic(rid, () -> {
            // todo 返回登录需要返回的信息

        });
        Future<Void> mapThreadLoginFuture = ThreadRouter.routing2Map(rid, () -> {
            // todo 登录地图
        });
        try {
            loginThreadLoginFuture.get();
            mapThreadLoginFuture.get();

        } catch (Exception e) {
            log.error("登录失败", e);
        }
        // 整个登录成功后，开启玩家心跳
        registerRoleHeart(role);
        LoginRoleRes loginRoleRes = new LoginRoleRes(role, 0, "", 0, 0);
        Context.sendMsgByUid(uid, loginRoleRes);
    }

    public void logoutRole(long rid) {
        Future<Void> logicThreadFuture = ThreadRouter.routing2Logic(rid, () -> {

        });
        Future<Void> mapThreadFuture = ThreadRouter.routing2Map(rid, () -> {

        });
        try {
            logicThreadFuture.get();
            mapThreadFuture.get();
        } catch (Exception e) {
            log.error("退出失败", e);
        }
        removeRoleHeart(rid);
        linkContainer.removeRid(rid);
    }

    public void registerRoleHeart(Role role) {
        RoleHeart roleHeart = rid2RoleHeart.putIfAbsent(role.getRid(), new RoleHeart(role));
        if (roleHeart != null) {
            log.error("玩家心跳已存在; rid:{}", role.getRid());
        }
    }

    public void removeRoleHeart(long rid) {
        RoleHeart remove = rid2RoleHeart.remove(rid);
        if (remove == null) {
            log.error("删除玩家心跳时,心跳不存在;rid:{}", rid);
        }
    }
}
