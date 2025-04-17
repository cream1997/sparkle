package com.cream.sparkle.common.object.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChunkData {
    private byte[] data;

    private Timestamp createTime;
    private Timestamp updateTime;
    private boolean deleted;
}
