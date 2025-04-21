package com.cream.sparkle.hero.net.msg.res;

import com.cream.sparkle.hero.net.constants.ResMsgType;
import com.cream.sparkle.hero.net.msg.BaseRes;
import com.cream.sparkle.hero.object.game.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRoleRes implements BaseRes {

    private Role role;
    private int mapId;
    private String mapName;
    private int mapWidth;
    private int mapHeight;

    @Override
    public ResMsgType msgType() {
        return ResMsgType.LoginRole;
    }
}
