package com.cream.sparkle.hero.manager;

import com.cream.sparkle.hero.object.game.Role;
import com.cream.sparkle.hero.tools.RoleDbTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoleManager {

    private final RoleDbTool roleDbTool;

    @Autowired
    public RoleManager(RoleDbTool roleDbTool) {
        this.roleDbTool = roleDbTool;
    }

    public Role getRole(long rid) {
        return this.roleDbTool.selectRole(rid);
    }
}
