package com.example.MyBank.configuration;

import com.example.MyBank.mappers.TransactionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class MyBankConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
