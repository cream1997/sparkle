package com.cream.sparkle.hero.service;

import com.cream.sparkle.global.error.TipErr;
import com.cream.sparkle.hero.mapper.AccountMapper;
import com.cream.sparkle.hero.obj.dto.LoginRes;
import com.cream.sparkle.hero.obj.entity.Account;
import com.cream.sparkle.utils.Nulls;
import com.cream.sparkle.utils.Strings;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    private final AccountMapper accountMapper;

    public AccountService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

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
        String token = UUID.randomUUID().toString();
        // todo token 放入缓存
        // 查询角色列表

        return new LoginRes(account.getId(), token);
    }
}
