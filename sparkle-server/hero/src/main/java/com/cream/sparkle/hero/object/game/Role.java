package com.cream.sparkle.hero.object.game;

public class Role {
    public final RoleBasic basic;

    public Role(long id, long uid, String name) {
        this.basic = new RoleBasic(id, uid, name);
    }
}
