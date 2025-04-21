package com.cream.sparkle.hero.controller;

import com.cream.sparkle.common.utils.HttpCtxUtil;
import com.cream.sparkle.hero.object.game.Role;
import com.cream.sparkle.hero.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/createRole")
    public Role createRole(String nickName) {
        long uid = HttpCtxUtil.getUid();
        return this.roleService.createRole(uid, nickName);
    }

    @PostMapping("/getAllRole")
    public List<Role> getAllRole() {
        long uid = HttpCtxUtil.getUid();
        return this.roleService.getAllRole(uid);
    }
}
