package com.cream.sparkle.hero.processor;

import com.cream.sparkle.hero.context.Context;
import com.cream.sparkle.hero.manager.MapManager;
import com.cream.sparkle.hero.manager.RoleManager;
import com.cream.sparkle.hero.net.constants.ReqMsgType;
import com.cream.sparkle.hero.net.msg.res.LoginRoleRes;
import com.cream.sparkle.hero.object.game.Role;
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

    /**
     * 这个Processor是个例外，第一个参数传来的是uid,因为在登录成功前，rid还未成功设置
     */
    @Override
    public void process(long uid, @NonNull Long rid) {
        log.info("收到LoginRole消息");
        Role role = this.roleManager.getRole(rid);
        // todo 登录...
        // todo 登录成功设置rid
        LoginRoleRes loginRoleRes = new LoginRoleRes(role, 0, "", 0, 0);
        Context.sendMsgByUid(uid, loginRoleRes);
    }

    @Override
    public ReqMsgType matchType() {
        return ReqMsgType.LoginRole;
    }
}
