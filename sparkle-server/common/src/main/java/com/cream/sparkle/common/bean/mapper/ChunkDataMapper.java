package com.cream.sparkle.common.bean.mapper;


import com.cream.sparkle.common.object.entity.ChunkData;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

public interface ChunkDataMapper {
    ChunkData selectChunkData(@Param("tableName") String tableName,
                              @Param("id") long id);

    void insertChunkData(@Param("tableName") String tableName,
                         @Param("id") long id,
                         @Param("data") byte[] data,
                         @Param("createTime") Timestamp createTime);

    void updateChunkData(@Param("tableName") String tableName,
                         @Param("id") long id,
                         @Param("data") byte[] data,
                         @Param("updateTime") Timestamp updateTime);
}
