package com.cream.sparkle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@MapperScan({"com.cream.sparkle.mapper", "com.cream.sparkle.z.hero.mapper"})
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class SparkleServerApp {

    public static void main(String[] args) {
        SpringApplication.run(SparkleServerApp.class, args);
    }
}
