package com.cream.sparkle.hero.processor;

import com.cream.sparkle.hero.manager.MapManager;
import com.cream.sparkle.hero.manager.RoleManager;
import com.cream.sparkle.hero.net.constants.ReqMsgType;
import com.cream.sparkle.hero.processor.base.LoginMsgProcessor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReqLoginRoleProcessor extends LoginMsgProcessor<Long> {

    private final RoleManager roleManager;


    public ReqLoginRoleProcessor(RoleManager roleManager, MapManager mapManager) {
        this.roleManager = roleManager;
    }

    /**
     * 这个Processor是个例外，第一个参数传来的是uid,因为在登录成功前，rid还未成功设置
     */
    @Override
    public void process(long uid, @NonNull Long rid) {
        this.roleManager.loginRole(uid, rid);
    }

    @Override
    public ReqMsgType matchType() {
        return ReqMsgType.LoginRole;
    }
}
