package com.cream.sparkle.hero.game.logic;

import com.cream.sparkle.common.utils.Times;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleBasic {
    public final long id;
    public final long uid;
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

    public RoleBasic(long id, long uid, String name) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.level = 1;
        this.createTime = Times.now();
    }
}
