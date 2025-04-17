package com.cream.sparkle.hero.object.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountInfo {

    @Getter
    private final long id;

    private final List<Long> allRid = new ArrayList<>();

    public List<Long> getAllRid() {
        return Collections.unmodifiableList(allRid);
    }

    public void addRid(long rid) {
        this.allRid.add(rid);
    }

    public void removeRid(long rid) {
        this.allRid.removeIf(it -> it.equals(rid));
    }

    public AccountInfo(long id) {
        this.id = id;
    }
}
