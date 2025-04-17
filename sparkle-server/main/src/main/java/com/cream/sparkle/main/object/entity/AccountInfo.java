package com.cream.sparkle.main.object.entity;

import lombok.Getter;

public class AccountInfo {

    @Getter
    private final long id;

    public AccountInfo(long id) {
        this.id = id;
    }
}
