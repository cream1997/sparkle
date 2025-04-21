package com.cream.sparkle.common.bean.tools;

import com.cream.sparkle.common.bean.mapper.ChunkDataMapper;
import com.cream.sparkle.common.object.entity.ChunkData;
import com.cream.sparkle.common.utils.DbCodecUtil;
import com.cream.sparkle.common.utils.Times;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 表字段是固定数量
 */
@Slf4j
@Component
public class ChunkDataTool {

    private final ChunkDataMapper chunkDataMapper;

    @Autowired
    public ChunkDataTool(ChunkDataMapper chunkDataMapper) {
        this.chunkDataMapper = chunkDataMapper;
    }

    public void insertChunkData(String tableName, long id, Object obj) {
        byte[] data = DbCodecUtil.format(obj);
        chunkDataMapper.insertChunkData(tableName, id, data, new Timestamp(Times.now()));
    }

    public <T> T selectChunkData(String tableName, long id, Class<T> clazz) {
        ChunkData chunkData = chunkDataMapper.selectChunkData(tableName, id);
        if (chunkData == null) {
            log.error("查询数据为空,tableName:{},id:{}", tableName, id);
            return null;
        }
        byte[] data = chunkData.getData();
        return DbCodecUtil.parse(data, clazz);
    }

    public void updateChunkData(String tableName, long id, Object obj) {
        byte[] data = DbCodecUtil.format(obj);
        chunkDataMapper.updateChunkData(tableName, id, data, new Timestamp(Times.now()));
    }
}
