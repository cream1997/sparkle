package com.cream.sparkle.hero;

import com.cream.sparkle.common.utils.Times;
import com.cream.sparkle.hero.object.game.Role;
import com.cream.sparkle.hero.tools.RoleDbTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class HeroSimpleTest {


    @Autowired
    private RoleDbTool roleDbTool;


    @Test
    public void test() {
        List<Role> roles = roleDbTool.selectAllRole(1745205110489L);
        System.out.println(roles);
    }

    @Test
    public void testInsertRole() {
        Role role = new Role(Times.now(), Times.now(), "张三");
        roleDbTool.insertRole(role);
    }
}
