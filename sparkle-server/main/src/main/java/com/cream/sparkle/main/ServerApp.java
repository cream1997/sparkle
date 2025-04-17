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

@MapperScan({"com.cream.sparkle.*.mapper"})
@SpringBootApplication(scanBasePackages = {"com.cream.sparkle"},
        // 排除springSecurity的默认认证配置
        exclude = {UserDetailsServiceAutoConfiguration.class})
public class ServerApp {

    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }
}

/**
 * 启动后初始化
 */
@Slf4j
@Configuration
class AppStartup implements ApplicationRunner {

    private final Environment env;

    public AppStartup(Environment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // todo 数据库version对比，存一些重要的实体类的结构，检查是否能对上，否则将来会序列化失败

        log.info("应用启动完成...");
    }
}