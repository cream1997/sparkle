package com.cream.sparkle.main;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@MapperScan({"com.cream.sparkle.main.mapper", "com.cream.sparkle.common.bean.mapper"})
@SpringBootApplication(
        scanBasePackages = {"com.cream.sparkle.main", "com.cream.sparkle.common.bean"},
        // 排除springSecurity的默认认证配置
        exclude = {UserDetailsServiceAutoConfiguration.class})
public class SparkleApp {

    public static void main(String[] args) {
        SpringApplication.run(SparkleApp.class, args);
    }
}

/**
 * 启动后初始化
 */
@Slf4j
@Configuration
class SparkleAppStartup implements ApplicationRunner {

    private final Environment env;

    public SparkleAppStartup(Environment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // todo 数据库version对比，存一些重要的实体类的结构，检查是否能对上，否则将来会序列化失败

        log.info("Sparkle Server启动完成...");
    }
}