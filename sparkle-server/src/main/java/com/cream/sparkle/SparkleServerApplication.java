package com.cream.sparkle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"com.cream.sparkle.mapper", "com.cream.sparkle.hero.mapper"})
@SpringBootApplication
public class SparkleServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SparkleServerApplication.class, args);
    }
}
