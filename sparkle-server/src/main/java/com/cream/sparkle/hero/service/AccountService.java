package com.cream.sparkle.hero.service;

import com.cream.sparkle.global.error.TipErr;
import com.cream.sparkle.hero.mapper.AccountMapper;
import com.cream.sparkle.hero.obj.entity.Account;
import com.cream.sparkle.utils.Strings;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountMapper accountMapper;

    public AccountService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    public void register(@NonNull String username, @NonNull String password) {
        int count = this.accountMapper.selectCountByUsername(username);
        if (count > 0) {
            throw new TipErr("用户名已存在！");
        }
        // 密码加密
        password = Strings.md5(password);
        Account account = new Account(username, password);
        this.accountMapper.insert(account);
    }
}
