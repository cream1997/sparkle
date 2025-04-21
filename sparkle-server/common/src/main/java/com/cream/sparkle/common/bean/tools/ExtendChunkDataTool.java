package com.cream.sparkle.common.bean.tools;

import com.cream.sparkle.common.bean.mapper.CommonMapper;
import com.cream.sparkle.common.utils.DbCodecUtil;
import com.cream.sparkle.common.utils.Nulls;
import com.cream.sparkle.common.utils.Times;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

/**
 * 表字段可以任意数量
 */
@Slf4j
@Component
public class ExtendChunkDataTool {

    private static final String DataTableFiledName = "data";
    private static final String CreateTimeTableFieldName = "createTime";
    private static final String UpdateTimeTableFieldName = "updateTime";

    private final CommonMapper commonMapper;

    @Autowired
    public ExtendChunkDataTool(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    public void insertExtendChunkData(String tableName, Object dataObj, Map<String, Object> extendFields) {
        if (Nulls.isBlank(tableName) || dataObj == null) {
            log.error("插入数据时,参数格式异常；tableName:{}, data:{}", tableName, dataObj);
            return;
        }
        Map<String, Object> valueMap = new HashMap<>();
        byte[] data = DbCodecUtil.format(dataObj);
        valueMap.put(DataTableFiledName, data);
        Timestamp nowTimeStamp = new Timestamp(Times.now());
        valueMap.put(CreateTimeTableFieldName, nowTimeStamp);
        valueMap.put(UpdateTimeTableFieldName, nowTimeStamp);
        if (extendFields != null) {
            valueMap.putAll(extendFields);
        }
        this.commonMapper.insert(tableName, valueMap);
    }

    public <T> List<T> selectAll(String tableName, Class<T> clazz, Map<String, Object> equalCondition) {
        if (Nulls.isBlank(tableName) || clazz == null) {
            log.error("查询数据时，参数格式异常；tableName:{}, clazz:{}", tableName, clazz);
            return Collections.emptyList();
        }
        /*
         * todo 返回的map中目前只用到了data,将来可以更加细化,比如对mapper的xml细化,因为扩展字段目前主要是用来查询使用比如一些索引
         */
        List<Map<String, Object>> allItem = this.commonMapper.selectAll(tableName, equalCondition);
        if (Nulls.isEmpty(allItem)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        for (Map<String, Object> objMap : allItem) {
            byte[] data = (byte[]) objMap.get(DataTableFiledName);
            T obj = DbCodecUtil.parse(data, clazz);
            result.add(obj);
        }
        return result;
    }

    public <T> T selectOne(String tableName, Class<T> clazz, Map<String, Object> equalCondition) {
        List<T> results = selectAll(tableName, clazz, equalCondition);
        if (results.size() != 1) {
            log.error("selectOne方法返回数量不为1");
            return null;
        }
        return results.getFirst();
    }

    public void update(String tableName, Object obj, Map<String, Object> extendFields, Map<String, Object> equalCondition) {
        if (Nulls.isBlank(tableName) || obj == null) {
            log.error("更新数据时，参数格式异常；tableName:{}, obj:{}", tableName, obj);
            return;
        }
        byte[] data = DbCodecUtil.format(obj);
        Map<String, Object> valueMap = extendFields != null ? new HashMap<>(extendFields) : new HashMap<>();
        valueMap.put(DataTableFiledName, data);
        valueMap.put(UpdateTimeTableFieldName, new Timestamp(Times.now()));
        this.commonMapper.update(tableName, valueMap, equalCondition);
    }

    /**
     * 仅更新data,不更新扩展字段
     */
    public void updateData(String tableName, Object obj, Map<String, Object> equalCondition) {
        update(tableName, obj, null, equalCondition);
    }
}
