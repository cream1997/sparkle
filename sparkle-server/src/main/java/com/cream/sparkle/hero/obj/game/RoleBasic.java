package com.cream.sparkle.hero.obj.game;

import com.cream.sparkle.utils.Times;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleBasic {
    private final long id;
    private String name;
    private int level;
    private int hp;
    private int mp;
    private int mapId;
    private int x;
    private int y;
    private final long createTime;
    private long loginTime;
    private long logoutTime;

    public RoleBasic(long id) {
        this.id = id;
        this.level = 1;
        this.createTime = Times.now();
    }
}
