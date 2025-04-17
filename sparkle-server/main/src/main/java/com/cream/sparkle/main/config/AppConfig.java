package com.cream.sparkle.main.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sparkle")
public class AppConfig {

    private String publishDir;
}
