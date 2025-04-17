package com.cream.sparkle.hero.tools;

import com.cream.sparkle.common.tools.ChunkDataTool;
import com.cream.sparkle.hero.object.game.Role;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.cream.sparkle.hero.constants.TableConstants.RoleTableName;


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
