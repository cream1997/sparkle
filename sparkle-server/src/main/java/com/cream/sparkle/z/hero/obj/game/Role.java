package com.cream.sparkle.z.hero.obj.game;

public class Role {
    public final RoleBasic basic;

    public Role(long id, String name) {
        this.basic = new RoleBasic(id, name);
    }
}
