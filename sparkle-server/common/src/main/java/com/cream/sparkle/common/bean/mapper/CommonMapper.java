package com.cream.sparkle.common.bean.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonMapper {

    //    @SuppressWarnings("all")
    List<Map<String, Object>> selectAll(@Param("tableName") String tableName,
                                        @Param("equalConditions") Map<String, Object> equalConditions);

    void insert(@Param("tableName") String tableName,
                @Param("valueMap") Map<String, Object> valueMap);

    void update(@Param("tableName") String tableName,
                @Param("valueMap") Map<String, Object> valueMap,
                @Param("equalConditions") Map<String, Object> equalConditions);
}
