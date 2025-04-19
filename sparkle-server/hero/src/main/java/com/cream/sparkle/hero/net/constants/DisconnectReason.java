package com.cream.sparkle.hero.net.constants;

public enum DisconnectReason {
    NetDisconnect("网络断开"),
    DingHao("顶号");

    public final String desc;

    DisconnectReason(String desc) {
        this.desc = desc;
    }
}
