package com.cream.sparkle.hero;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@MapperScan({"com.cream.sparkle.hero.mapper", "com.cream.sparkle.common.bean.mapper"})
@SpringBootApplication(scanBasePackages = {"com.cream.sparkle.hero", "com.cream.sparkle.common.bean"},
        // 排除springSecurity的默认认证配置
        exclude = {UserDetailsServiceAutoConfiguration.class})
public class HeroServer {

    public static void main(String[] args) {
        SpringApplication.run(HeroServer.class, args);
    }
}

@Slf4j
@Configuration
class HeroServerStartup implements ApplicationRunner {

    private final Environment env;

    HeroServerStartup(Environment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("Hero Server 启动完成...");
    }
}
