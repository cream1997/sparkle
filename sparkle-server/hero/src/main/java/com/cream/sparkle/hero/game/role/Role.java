package com.cream.sparkle.hero.game.role;

public class Role {
    public final RoleBasic basic;

    public Role(long id, long uid, String name) {
        this.basic = new RoleBasic(id, uid, name);
    }

    public long getRid() {
        return basic.id;
    }
}
