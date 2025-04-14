package com.cream.sparkle.mapper;


import com.cream.sparkle.object.entity.ChunkData;
import org.apache.ibatis.annotations.Param;

public interface ChunkDataMapper {
    ChunkData selectChunkData(@Param("tableName") String tableName, @Param("id") long id);

    void insertChunkData(@Param("tableName") String tableName, long id, byte[] data);

    void updateChunkData(@Param("tableName") String tableName, long id, byte[] data);
}
