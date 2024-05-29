package com.example.MyBank.controller;


import com.example.MyBank.model.balance.BalanceResponse;
import com.example.MyBank.model.moneyTransfer.request.MoneyTransferRequest;
import com.example.MyBank.model.moneyTransfer.response.MoneyTransferResponse;
import com.example.MyBank.model.transaction.TransactionResponse;
import com.example.MyBank.service.BankService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/bank")
public class BankController {

    @Autowired
    private BankService bankService;


    @GetMapping("/balance/{accountId}")
    public BalanceResponse getCashAccountBalance(@PathVariable String accountId) throws JsonProcessingException {

        return bankService.getCashAccountBalance(accountId);
    }

    @GetMapping("/transaction/{accountId}")
    public TransactionResponse getCashAccountTransactions(@PathVariable String accountId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date fromAccountingDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Date toAccountingDate) throws JsonProcessingException {
        return bankService.getCashAccountTransactions(accountId, fromAccountingDate, toAccountingDate);
    }

    @PostMapping("/money-transfer/{accountId}")
    public MoneyTransferResponse createMoneyTransfer(@PathVariable String accountId, @RequestBody MoneyTransferRequest moneyTransferRequest) throws JsonProcessingException {
        return bankService.createMoneyTransfer(accountId, moneyTransferRequest);
    }
}
