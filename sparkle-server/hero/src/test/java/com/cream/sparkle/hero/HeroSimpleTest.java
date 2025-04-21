package com.cream.sparkle.hero;

import com.cream.sparkle.common.bean.mapper.CommonMapper;
import com.cream.sparkle.common.bean.tools.ExtendChunkDataTool;
import com.cream.sparkle.common.utils.Times;
import com.cream.sparkle.hero.object.game.Role;
import com.cream.sparkle.hero.tools.RoleDbTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
public class HeroSimpleTest {

    @Autowired
    private ExtendChunkDataTool extendChunkDataTool;

    @Autowired
    private RoleDbTool roleDbTool;

    @Autowired
    private CommonMapper commonMapper;

    @Test
    public void test() {
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("id", 1745198591582L);
        Role role = extendChunkDataTool.selectOne("hero_role", Role.class, condition);
        System.out.println(role);
    }

    @Test
    public void testInsertRole() {
        Role role = new Role(Times.now(), "李四");
        roleDbTool.insertRole(role);
    }
}
