package com.example.oauth.config;

import com.netflix.appinfo.EurekaInstanceConfig;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

    @Bean
    RestTemplate template() {
        return new RestTemplate();
    }
}
