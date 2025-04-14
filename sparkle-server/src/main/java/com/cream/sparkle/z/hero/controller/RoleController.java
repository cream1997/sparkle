package com.cream.sparkle.z.hero.controller;

import com.cream.sparkle.z.hero.obj.game.Role;
import com.cream.sparkle.z.hero.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/hero")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * todo uid不应该客户端传，token验证也没有做
     */
    @PostMapping("/createRole")
    public Role createRole(long uid, String nickName) {
        return this.roleService.createRole(uid, nickName);
    }
}
