package com.example.MyBank.service;

import com.example.MyBank.configuration.SandboxConfig;
import com.example.MyBank.mappers.TransactionMapper;
import com.example.MyBank.model.balance.BalanceResponse;
import com.example.MyBank.model.moneyTransfer.request.MoneyTransferRequest;
import com.example.MyBank.model.moneyTransfer.response.MoneyTransferResponse;
import com.example.MyBank.model.transaction.Transaction;
import com.example.MyBank.model.transaction.TransactionResponse;
import com.example.MyBank.repository.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@Slf4j
public class BankService {
    @Autowired
    private SandboxConfig sandboxConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionMapper transactionMapper;

    public BalanceResponse getCashAccountBalance(String accountId) throws JsonProcessingException {
        BalanceResponse response;
        try {
            String url = sandboxConfig.getBaseUrl() + "/api/gbs/banking/v4.0/accounts/" + accountId + "/balance";

            HttpHeaders httpHeaders = createHttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            response = restTemplate.exchange(url, HttpMethod.GET, entity, BalanceResponse.class).getBody();

            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            response = objectMapper.readValue(e.getResponseBodyAsString(), BalanceResponse.class);
            response.setPayload(null);
            return response;
        }

    }

    public TransactionResponse getCashAccountTransactions(String accountId, Date fromAccountingDate, Date toAccountingDate) throws JsonProcessingException {
        TransactionResponse response;
        try {
            String baseUrl = sandboxConfig.getBaseUrl() + "/api/gbs/banking/v4.0/accounts/" + accountId + "/transactions";

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fromDateString = dateFormat.format(fromAccountingDate);
            String toDateString = dateFormat.format(toAccountingDate);

            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("fromAccountingDate", fromDateString)
                    .queryParam("toAccountingDate", toDateString)
                    .toUriString();

            HttpHeaders httpHeaders = createHttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            response = restTemplate.exchange(url, HttpMethod.GET, entity, TransactionResponse.class).getBody();

            for(Transaction transaction : response.getPayload().getList()) {
                transactionRepository.save(transactionMapper.map(transaction));
            }
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            response = objectMapper.readValue(e.getResponseBodyAsString(), TransactionResponse.class);
            return response;
        }
    }

    public MoneyTransferResponse createMoneyTransfer(String accountId, MoneyTransferRequest moneyTransferRequest) throws JsonProcessingException {
        MoneyTransferResponse response;
        try {
            String url = sandboxConfig.getBaseUrl() + "/api/gbs/banking/v4.0/accounts/" + accountId + "/payments/money-transfers";

            HttpHeaders httpHeaders = createHttpHeaders();
            HttpEntity<MoneyTransferRequest> entity = new HttpEntity<>(moneyTransferRequest, httpHeaders);

            response = restTemplate.exchange(url, HttpMethod.POST, entity, MoneyTransferResponse.class).getBody();

            return response;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            response = objectMapper.readValue(e.getResponseBodyAsString(), MoneyTransferResponse.class);
            return response;
        }
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Auth-Schema", sandboxConfig.getAuthSchema());
        httpHeaders.set("Api-Key", sandboxConfig.getApiKey());
        httpHeaders.set("X-Time-Zone", "Europe/Rome");
        return httpHeaders;
    }

}
