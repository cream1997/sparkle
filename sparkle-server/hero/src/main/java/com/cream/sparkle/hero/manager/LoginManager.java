package com.cream.sparkle.hero.manager;

import com.cream.sparkle.hero.net.component.ThreadRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * 主要的功能是将登录线程和玩家线程串联到一起
 */
@Slf4j
@Component
public class LoginManager {


    private final RoleManager roleManager;

    @Autowired
    public LoginManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void login(long uid, long rid) {
        Future<Void> loginThreadLoginFuture = ThreadRouter.routing2Role(rid, () -> roleManager.enterRole(uid, rid));
        try {
            // 阻塞操作，主要的目的是保证线程一致性(登录线程与玩家线程)
            loginThreadLoginFuture.get();
        } catch (Exception e) {
            log.error("登录异常", e);
        }
    }

    public void logout(long rid) {
        Future<Void> logicThreadFuture = ThreadRouter.routing2Role(rid, () -> roleManager.exitRole(rid));
        try {
            // 阻塞操作，主要的目的是保证线程一致性(登录线程与玩家线程)
            logicThreadFuture.get();
        } catch (Exception e) {
            log.error("退出异常", e);
        }
    }
}
