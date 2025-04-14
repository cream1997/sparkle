package com.cream.sparkle.z.hero.obj.game;

import com.cream.sparkle.utils.Times;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleBasic {
    public final long id;
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

    public RoleBasic(long id, String name) {
        this.id = id;
        this.name = name;
        this.level = 1;
        this.createTime = Times.now();
    }
}
