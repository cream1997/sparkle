package com.cream.sparkle.config.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class AppWebMvcConfigurer implements WebMvcConfigurer {


    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 完全放开跨域
        registry
                .addMapping("/**") //添加映射路径
                .allowCredentials(true) //是否发送Cookie
                .allowedOriginPatterns("*") //允许跨域的域名，*代表允许任何域名
                .allowedMethods("*") //允许任何方法（post、get等）
                .exposedHeaders("*"); //允许任何头
    }
}
