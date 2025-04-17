package com.cream.sparkle.hero.service;

import com.cream.sparkle.common.error.TipErr;
import com.cream.sparkle.common.utils.Nulls;
import com.cream.sparkle.common.utils.Times;
import com.cream.sparkle.hero.object.entity.AccountInfo;
import com.cream.sparkle.hero.object.game.Role;
import com.cream.sparkle.hero.tools.AccountInfoDbTool;
import com.cream.sparkle.hero.tools.RoleDbTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RoleService {

    private final AccountInfoDbTool accountInfoDbTool;
    private final RoleDbTool roleDbTool;

    @Autowired
    public RoleService(AccountInfoDbTool accountInfoDbTool, RoleDbTool roleDbTool) {
        this.accountInfoDbTool = accountInfoDbTool;
        this.roleDbTool = roleDbTool;
    }

    @Transactional(rollbackFor = Exception.class)
    public Role createRole(long uid, String nickName) {
        if (Nulls.isBlank(nickName)) {
            throw new TipErr("昵称不能为空");
        }
        // fixme 随机id算法
        Role role = new Role(Times.now(), nickName);
        this.roleDbTool.insertRole(role);
        AccountInfo accountInfo = accountInfoDbTool.selectAccountInfo(uid);
        accountInfo.addRid(role.basic.id);
        this.accountInfoDbTool.updateAccountInfo(accountInfo);
        return role;
    }
}
