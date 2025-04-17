package com.cream.sparkle.z.hero.tools;

import com.cream.sparkle.tools.ChunkDataTool;
import com.cream.sparkle.z.hero.obj.game.Role;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.cream.sparkle.z.hero.constant.TableConstants.RoleTableName;

@Slf4j
@Component
public class RoleDbTool {

    private final ChunkDataTool chunkDataTool;

    @Autowired
    public RoleDbTool(ChunkDataTool chunkDataTool) {
        this.chunkDataTool = chunkDataTool;
    }

    public void insertRole(@NonNull Role role) {
        this.chunkDataTool.insertChunkData(RoleTableName, role.basic.id, role);
    }

    public Role selectRole(long rid) {
        return this.chunkDataTool.selectChunkData(RoleTableName, rid, Role.class);
    }

    public void updateRole(@NonNull Role role) {
        this.chunkDataTool.updateChunkData(RoleTableName, role.basic.id, role);
    }
}
