package com.example.MyBank.service;

import com.example.MyBank.configuration.SandboxConfig;
import com.example.MyBank.exception.AccountBalanceNotFoundException;
import com.example.MyBank.exception.MoneyTransferException;
import com.example.MyBank.exception.TransactionException;
import com.example.MyBank.model.ResponseError;
import com.example.MyBank.model.balance.BalanceResponse;
import com.example.MyBank.model.moneyTransfer.request.MoneyTransferRequest;
import com.example.MyBank.model.moneyTransfer.response.MoneyTransferResponse;
import com.example.MyBank.model.transaction.TransactionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
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

    public BalanceResponse getCashAccountBalance() throws AccountBalanceNotFoundException {

            String url= sandboxConfig.getBaseUrl()+ "/api/gbs/banking/v4.0/accounts/" + sandboxConfig.getAccountId() + "/balance";

            HttpHeaders httpHeaders= createHttpHeaders();
            HttpEntity<String> entity= new HttpEntity<>(httpHeaders);

            ResponseEntity<BalanceResponse> response= callApi(url, HttpMethod.GET, entity, BalanceResponse.class);

            if(response.getBody().getStatus().equals("OK")){
                return response.getBody();
            }else {
                ResponseError error = response.getBody().getError()[0];
                throw new AccountBalanceNotFoundException(error.getCode(), error.getDescription());
            }
    }


    public TransactionResponse getCashAccountTransactions(Date fromAccountingDate, Date toAccountingDate) throws TransactionException {

            String baseUrl = sandboxConfig.getBaseUrl() + "/api/gbs/banking/v4.0/accounts/" + sandboxConfig.getAccountId() + "/transactions";

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fromDateString = dateFormat.format(fromAccountingDate);
            String toDateString = dateFormat.format(toAccountingDate);

            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("fromAccountingDate", fromDateString)
                    .queryParam("toAccountingDate", toDateString)
                    .toUriString();

            HttpHeaders httpHeaders = createHttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            ResponseEntity<TransactionResponse> response = callApi(url, HttpMethod.GET, entity, TransactionResponse.class);

            if (response.getBody().getStatus().equals("OK")) {
                return response.getBody();
            } else {
                ResponseError error = response.getBody().getError()[0];
                throw new TransactionException(error.getCode(), error.getDescription());
            }
    }

    public MoneyTransferResponse createMoneyTransfer(MoneyTransferRequest moneyTransferRequest) throws MoneyTransferException {
            String url= sandboxConfig.getBaseUrl()+ "/api/gbs/banking/v4.0/accounts/" + sandboxConfig.getAccountId() + "/payments/money-transfers";

            HttpHeaders httpHeaders= createHttpHeaders();
            HttpEntity<MoneyTransferRequest> entity = new HttpEntity<>(moneyTransferRequest, httpHeaders);

            ResponseEntity<MoneyTransferResponse> response= callApi(url, HttpMethod.POST, entity, MoneyTransferResponse.class);

            if(response.getBody().getStatus().equals("OK")){
                return response.getBody();
            }else {
                ResponseError error = response.getBody().getError()[0];
                throw new MoneyTransferException(error.getCode(), error.getDescription());
            }
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Auth-Schema", sandboxConfig.getAuthSchema());
        httpHeaders.set("Api-Key", sandboxConfig.getApiKey());
        httpHeaders.set("X-Time-Zone", "Europe/Rome");
        return httpHeaders;
    }

    private <T> ResponseEntity<T> callApi(String url, HttpMethod method, HttpEntity<?> entity, Class<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(url, method, entity, responseType);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(response.getStatusCode(), "Failed to call API");
            }
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage(), e);
        } catch (Exception e) {
            log.error("Exception occurred: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}
