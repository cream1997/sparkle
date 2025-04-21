package com.cream.sparkle.hero.processor;

import com.cream.sparkle.hero.manager.MapManager;
import com.cream.sparkle.hero.manager.RoleManager;
import com.cream.sparkle.hero.net.constants.ReqMsgType;
import com.cream.sparkle.hero.processor.base.MsgProcessor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReqLoginRoleProcessor extends MsgProcessor<Long> {

    private final RoleManager roleManager;
    private final MapManager mapManager;

    public ReqLoginRoleProcessor(RoleManager roleManager, MapManager mapManager) {
        this.roleManager = roleManager;
        this.mapManager = mapManager;
    }

    @Override
    public void process(long _ignore, @NonNull Long rid) {
        log.info("收到LoginRole消息");
    }

    @Override
    public ReqMsgType matchType() {
        return ReqMsgType.LoginRole;
    }
}
