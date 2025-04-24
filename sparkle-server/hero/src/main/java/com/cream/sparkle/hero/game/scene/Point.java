package com.cream.sparkle.hero.game.scene;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Point {
    public final int x;
    public final int y;
    public boolean block;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, boolean block) {
        this.x = x;
        this.y = y;
        this.block = block;
    }
}
