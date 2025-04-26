package com.cream.sparkle.hero.manager;

import com.cream.sparkle.hero.game.role.Role;
import com.cream.sparkle.hero.tools.RoleDbTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class RoleManager {

    private final RoleDbTool roleDbTool;

    @Autowired
    public RoleManager(RoleDbTool roleDbTool) {
        this.roleDbTool = roleDbTool;
    }

    private Role getRole(long rid) {
        Role role = this.roleDbTool.selectRole(rid);
        Objects.requireNonNull(role);
        return role;
    }
}
