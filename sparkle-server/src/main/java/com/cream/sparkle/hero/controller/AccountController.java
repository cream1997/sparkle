package com.cream.sparkle.hero.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/hero")
public class AccountController {

    @PostMapping("/register")
    public void register(String username, String password) {
        log.info("收到");
    }
}
