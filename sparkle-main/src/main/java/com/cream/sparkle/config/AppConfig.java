package com.cream.sparkle.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sparkle")
public class AppConfig {

    private String publishDir;
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