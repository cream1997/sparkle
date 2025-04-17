package com.cream.sparkle.main.tools;

import com.cream.sparkle.common.bean.tools.ChunkDataTool;
import com.cream.sparkle.main.object.entity.AccountInfo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.cream.sparkle.common.constants.TableConstants.AccountInfoTableName;


@Slf4j
@Component
public class AccountInfoDbTool {


    private final ChunkDataTool chunkDataTool;

    @Autowired
    public AccountInfoDbTool(ChunkDataTool chunkDataTool) {
        this.chunkDataTool = chunkDataTool;
    }

    public void insertAccountInfo(@NonNull AccountInfo accountInfo) {
        this.chunkDataTool.insertChunkData(AccountInfoTableName, accountInfo.getId(), accountInfo);
    }

    public AccountInfo selectAccountInfo(long id) {
        return this.chunkDataTool.selectChunkData(AccountInfoTableName, id, AccountInfo.class);
    }

    public void updateAccountInfo(@NonNull AccountInfo accountInfo) {
        this.chunkDataTool.updateChunkData(AccountInfoTableName, accountInfo.getId(), accountInfo);
    }
}
