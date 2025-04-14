package com.cream.sparkle.tools;

import com.alibaba.fastjson2.JSON;
import com.cream.sparkle.mapper.ChunkDataMapper;
import com.cream.sparkle.object.entity.ChunkData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ChunkDataTool {

    private final ChunkDataMapper chunkDataMapper;

    @Autowired
    public ChunkDataTool(ChunkDataMapper chunkDataMapper) {
        this.chunkDataMapper = chunkDataMapper;
    }

    public void insertChunkData(String tableName, long id, Object obj) {
        byte[] data = format(obj);
        chunkDataMapper.insertChunkData(tableName, id, data);
    }

    public <T> T selectChunkData(String tableName, long id, Class<T> clazz) {
        ChunkData chunkData = chunkDataMapper.selectChunkData(tableName, id);
        if (chunkData == null) {
            log.error("查询数据为空,tableName:{},id:{}", tableName, id);
            return null;
        }
        byte[] data = chunkData.getData();
        return parse(data, clazz);
    }

    public void updateChunkData(String tableName, long id, Object obj) {
        byte[] data = format(obj);
        chunkDataMapper.updateChunkData(tableName, id, data);
    }

    private byte[] format(Object obj) {
        return JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8);
    }

    private <T> T parse(byte[] data, Class<T> tClass) {
        String jsonStr = new String(data, StandardCharsets.UTF_8);
        return JSON.parseObject(jsonStr, tClass);
    }
}
