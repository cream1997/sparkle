package com.cream.sparkle.hero.controller;

import com.cream.sparkle.hero.obj.dto.LoginRes;
import com.cream.sparkle.hero.service.AccountService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/hero")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public void register(@NonNull String username, @NonNull String password) {
        this.accountService.register(username, password);
    }

    @PostMapping("/login")
    public LoginRes login(String username, String password) {
        return this.accountService.login(username, password);
    }
}
