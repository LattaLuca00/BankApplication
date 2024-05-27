package com.example.MyBank.configuration;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyBankConfig {

    @Bean
    public RestTemplate restTemplate( ) {
        return new RestTemplate();
    }
}
