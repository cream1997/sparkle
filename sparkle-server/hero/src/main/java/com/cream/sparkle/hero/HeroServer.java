package com.cream.sparkle.hero;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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

@Slf4j
@Component
class HeroServerCleanup implements DisposableBean {

    /**
     * 这个类依赖的bean会在destroy方法结束后清理
     * todo 应用程序正常结束，统一做一些收尾工作，比如清理任务，数据持久化任务，
     * 程序在收到kill -15后，jvm会自动结束各个线程(包括用户线程)，
     * 一旦线程或线程池shutdown/shutdownNow了， 就不能提交任务、执行任务了;
     * 也就是说结束任务不要依赖于其他线程去执行，否则可能会执行不到，结束任务尽量在这个方法里直接调用，
     * <p>
     * 实测发现自定义线程池的关闭会在这个方法结束之后，也就是在这个方法里似乎还能利用线程池提交任务，
     * 但是能直接调用的话，尽量直接调用
     */
    @Override
    public void destroy() throws Exception {
        log.info("程序正常停止...");
    }
}