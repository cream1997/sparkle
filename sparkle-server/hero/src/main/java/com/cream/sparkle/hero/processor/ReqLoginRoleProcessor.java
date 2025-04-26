package com.cream.sparkle.hero.processor;

import com.cream.sparkle.hero.manager.LoginManager;
import com.cream.sparkle.hero.net.constants.ReqMsgType;
import com.cream.sparkle.hero.processor.base.LoginMsgProcessor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReqLoginRoleProcessor extends LoginMsgProcessor<Long> {


    private final LoginManager loginManager;

    public ReqLoginRoleProcessor(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    /**
     * 这个Processor是个例外，第一个参数传来的是uid,因为在登录成功前，rid还未成功设置
     */
    @Override
    public void process(long uid, @NonNull Long rid) {
        this.loginManager.loginRole(uid, rid);
    }

    @Override
    public ReqMsgType matchType() {
        return ReqMsgType.LoginRole;
    }
}
