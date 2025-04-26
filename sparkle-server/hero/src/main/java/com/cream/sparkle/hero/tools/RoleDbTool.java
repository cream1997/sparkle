package com.cream.sparkle.hero.tools;

import com.cream.sparkle.common.bean.tools.ExtendChunkDataTool;
import com.cream.sparkle.hero.game.role.Role;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cream.sparkle.common.constants.TableConstants.RoleTableName;


@Slf4j
@Component
public class RoleDbTool {

    private final ExtendChunkDataTool extendChunkDataTool;

    @Autowired
    public RoleDbTool(ExtendChunkDataTool extendChunkDataTool) {
        this.extendChunkDataTool = extendChunkDataTool;
    }

    public void insertRole(@NonNull Role role) {
        Map<String, Object> extendFields = new HashMap<>();
        extendFields.put("id", role.basic.id);
        extendFields.put("uid", role.basic.uid);
        this.extendChunkDataTool.insertExtendChunkData(RoleTableName, role, extendFields);
    }

    public Role selectRole(long rid) {
        Map<String, Object> equalConditions = new HashMap<>();
        equalConditions.put("id", rid);
        return this.extendChunkDataTool.selectOne(RoleTableName, Role.class, equalConditions);
    }

    public List<Role> selectAllRole(long uid) {
        Map<String, Object> equalConditions = new HashMap<>();
        equalConditions.put("uid", uid);
        return this.extendChunkDataTool.selectAll(RoleTableName, Role.class, equalConditions);
    }

    public void updateRole(@NonNull Role role) {
        Map<String, Object> equalConditions = new HashMap<>();
        equalConditions.put("id", role.basic.id);
        this.extendChunkDataTool.updateData(RoleTableName, role, equalConditions);
    }
}
