package com.cream.sparkle.hero.context;

import com.cream.sparkle.hero.game.role.Role;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoleHeart {

    private final Role role;

    public RoleHeart(Role role) {
        this.role = role;
    }

    public void heartPerSecond() {
//        log.debug("玩家：{}, 心跳执行", role.basic.getName());
    }
}
