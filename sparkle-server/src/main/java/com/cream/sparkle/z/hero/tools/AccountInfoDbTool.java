package com.cream.sparkle.z.hero.tools;

import com.cream.sparkle.tools.ChunkDataTool;
import com.cream.sparkle.z.hero.constant.TableConstants;
import com.cream.sparkle.z.hero.obj.entity.AccountInfo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountInfoDbTool {


    private final ChunkDataTool chunkDataTool;

    @Autowired
    public AccountInfoDbTool(ChunkDataTool chunkDataTool) {
        this.chunkDataTool = chunkDataTool;
    }

    public void insertAccountInfo(@NonNull AccountInfo accountInfo) {
        this.chunkDataTool.insertChunkData(TableConstants.AccountInfoTableName, accountInfo.getId(), accountInfo);
    }

    public AccountInfo selectAccountInfo(long id) {
        return this.chunkDataTool.selectChunkData(TableConstants.AccountInfoTableName, id, AccountInfo.class);
    }

    public void updateAccountInfo(@NonNull AccountInfo accountInfo) {
        this.chunkDataTool.updateChunkData(TableConstants.AccountInfoTableName, accountInfo.getId(), accountInfo);
    }
}
