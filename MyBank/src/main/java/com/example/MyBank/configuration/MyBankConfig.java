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

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("PITC-Zscaler-Global-ZEN.proxy.corporate.ge.com", 80));
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);

        return new RestTemplate(requestFactory);
    }

}
