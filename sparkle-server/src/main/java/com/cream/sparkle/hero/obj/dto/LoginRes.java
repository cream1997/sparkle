package com.cream.sparkle.hero.obj.dto;

import lombok.Getter;

@Getter
public class LoginRes {
    private final long id;
    private final String token;

    public LoginRes(long id, String token) {
        this.id = id;
        this.token = token;
    }
}
