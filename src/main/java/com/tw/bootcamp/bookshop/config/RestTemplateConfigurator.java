package com.tw.bootcamp.bookshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfigurator {

    @Bean
    public static RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
