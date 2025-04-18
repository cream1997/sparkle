package com.cream.sparkle.main.object.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.cream.sparkle.common.constants.TableConstants;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@TableName(TableConstants.AccountTableName)
public class Account {
    /**
     * 雪花算法生成id
     * ※ 这里因为得自动赋值要使用包装类，可能会是因为mybatis不把0作为null考虑，而不自动赋值
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("username")
    private final String username;

    @TableField("password")
    private String password;
    /**
     * 逻辑删除
     */
    @Setter
    @TableLogic
    @TableField("deleted")
    private boolean deleted;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    private Timestamp createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;


    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account() {
        this.username = null;
    }
}
