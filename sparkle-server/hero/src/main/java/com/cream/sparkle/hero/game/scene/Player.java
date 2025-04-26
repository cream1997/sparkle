package com.cream.sparkle.hero.game.scene;

import lombok.Getter;
import lombok.Setter;

public class Player {

    public final long rid;
    public final String name;

    @Setter
    @Getter
    private int level;
    @Setter
    @Getter
    private int hp;
    @Setter
    @Getter
    private int mp;
    @Setter
    @Getter
    private int x;
    @Setter
    @Getter
    private int y;

    public Player(long rid, String name) {
        this.rid = rid;
        this.name = name;
    }
}
