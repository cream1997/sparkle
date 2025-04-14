package com.cream.sparkle.hero.obj.game;

import lombok.Getter;

@Getter
public class Role {
    private final RoleBasic roleBasic;

    public Role(long id) {
        this.roleBasic = new RoleBasic(id);
    }
}
