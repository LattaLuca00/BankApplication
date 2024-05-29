package com.example.MyBank.configuration;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SandboxConfig {

    @Value("${sandbox.base-url}")
    private String baseUrl;

    @Value("${sandbox.auth-schema}")
    private String authSchema;

    @Value("${sandbox.api-key}")
    private String apiKey;


}
