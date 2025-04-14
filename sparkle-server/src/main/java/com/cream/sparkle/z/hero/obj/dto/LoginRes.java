package com.cream.sparkle.z.hero.obj.dto;

import com.cream.sparkle.z.hero.obj.game.Role;
import lombok.Getter;

import java.util.List;

@Getter
public class LoginRes {
    private final long id;
    private final String token;
    private final List<Role> allRole;

    public LoginRes(long id, String token, List<Role> allRole) {
        this.id = id;
        this.token = token;
        this.allRole = allRole;
    }
}
