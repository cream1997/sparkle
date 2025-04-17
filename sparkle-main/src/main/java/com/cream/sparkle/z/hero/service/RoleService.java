package com.cream.sparkle.z.hero.service;

import com.cream.sparkle.global.error.TipErr;
import com.cream.sparkle.utils.Nulls;
import com.cream.sparkle.utils.Times;
import com.cream.sparkle.z.hero.obj.entity.AccountInfo;
import com.cream.sparkle.z.hero.obj.game.Role;
import com.cream.sparkle.z.hero.tools.AccountInfoDbTool;
import com.cream.sparkle.z.hero.tools.RoleDbTool;
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
