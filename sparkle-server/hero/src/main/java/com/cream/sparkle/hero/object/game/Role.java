package com.cream.sparkle.hero.object.game;

public class Role {
    public final RoleBasic basic;

    public Role(long id, String name) {
        this.basic = new RoleBasic(id, name);
    }
}
