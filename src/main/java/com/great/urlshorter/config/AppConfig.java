package com.great.urlshorter.config;

import com.great.urlshorter.utils.SnowFlake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Great
 */
@Configuration
public class AppConfig {

    @Bean
    public SnowFlake snowFlake() {
        return new SnowFlake(1, 1);
    }
}
