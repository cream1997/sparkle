package com.cream.sparkle.hero;

import com.cream.sparkle.hero.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HeroSimpleTest {

    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void testInsert() {
//        Account zs = new Account("张三", "123");
//        accountMapper.insert(zs);
    }
}
