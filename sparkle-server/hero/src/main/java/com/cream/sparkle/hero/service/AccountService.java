package com.cream.sparkle.hero.service;

import com.cream.sparkle.common.error.TipErr;
import com.cream.sparkle.common.security.JwtUtil;
import com.cream.sparkle.common.security.QuanXianEnum;
import com.cream.sparkle.common.utils.Nulls;
import com.cream.sparkle.common.utils.Strings;
import com.cream.sparkle.hero.mapper.AccountMapper;
import com.cream.sparkle.hero.object.dto.LoginRes;
import com.cream.sparkle.hero.object.entity.Account;
import com.cream.sparkle.hero.object.entity.AccountInfo;
import com.cream.sparkle.hero.object.game.Role;
import com.cream.sparkle.hero.tools.AccountInfoDbTool;
import com.cream.sparkle.hero.tools.RoleDbTool;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountInfoDbTool accountInfoDbTool;
    private final RoleDbTool roleDbTool;

    @Autowired
    public AccountService(AccountMapper accountMapper,
                          AccountInfoDbTool accountInfoDbTool,
                          RoleDbTool roleDbTool) {
        this.accountMapper = accountMapper;
        this.accountInfoDbTool = accountInfoDbTool;
        this.roleDbTool = roleDbTool;
    }

    @Transactional(rollbackFor = Exception.class)
    public void register(@NonNull String username, @NonNull String password) {
        if (Nulls.anyBlank(username, password)) {
            throw new TipErr("输入不合法");
        }
        int count = this.accountMapper.selectCountByUsername(username);
        if (count > 0) {
            throw new TipErr("用户名已存在！");
        }
        // 密码加密
        password = Strings.md5(password);
        Account account = new Account(username, password);
        this.accountMapper.insert(account);
        // 插入之后会生成id
        AccountInfo accountInfo = new AccountInfo(account.getId());
        this.accountInfoDbTool.insertAccountInfo(accountInfo);
    }

    public LoginRes login(String username, String password) {
        if (Nulls.anyBlank(username, password)) {
            throw new TipErr("输入不合法");
        }
        Account account = this.accountMapper.selectByUsername(username);
        if (account == null) {
            throw new TipErr("账号不存在");
        }
        if (!account.getPassword().equals(Strings.md5(password))) {
            throw new TipErr("密码错误");
        }
        // 生成token,客户端携带token建立ws/socket连接
        String token = JwtUtil.generateToken(account.getId(), QuanXianEnum.HERO.name());
        // todo token 放入缓存
        // 查询角色列表
        AccountInfo accountInfo = this.accountInfoDbTool.selectAccountInfo(account.getId());
        Objects.requireNonNull(accountInfo);

        List<Role> allRole = accountInfo.getAllRid().stream()
                // fixme 循环查询改为批量查询，尽管这里数量不大
                .map(this.roleDbTool::selectRole)
                .toList();
        return new LoginRes(account.getId(), token, allRole);
    }
}
