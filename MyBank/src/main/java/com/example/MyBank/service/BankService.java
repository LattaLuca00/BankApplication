package com.example.MyBank.service;

import com.example.MyBank.configuration.SandboxConfig;
import com.example.MyBank.model.Balance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class BankService {

    @Autowired
    private SandboxConfig sandboxConfig;
    @Autowired
    private RestTemplate restTemplate;

    public Balance getSaldo() {
        try {
            String url= sandboxConfig.getBaseUrl()+ "/api/gbs/banking/v4.0/accounts";

            HttpHeaders httpHeaders= new HttpHeaders();
            httpHeaders.set("Auth-Schema", sandboxConfig.getAuthSchema());
            httpHeaders.set("Api-Key", sandboxConfig.getApiKey());
            httpHeaders.set("X-Time-Zone","Europe/Rome");

            HttpEntity<String> entity= new HttpEntity<>(httpHeaders);

            ResponseEntity<Balance> response= restTemplate.exchange(url, HttpMethod.GET, entity, Balance.class);

            return response.getBody();
        }catch (Exception e){
            log.error(e.getMessage());

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error");
        }
    }
}
