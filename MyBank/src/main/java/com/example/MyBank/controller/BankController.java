package com.example.MyBank.controller;


import com.example.MyBank.exception.AccountBalanceNotFoundException;
import com.example.MyBank.exception.MoneyTransferException;
import com.example.MyBank.exception.TransactionException;
import com.example.MyBank.model.balance.BalanceResponse;
import com.example.MyBank.model.moneyTransfer.request.MoneyTransferRequest;
import com.example.MyBank.model.moneyTransfer.response.MoneyTransferResponse;
import com.example.MyBank.model.transaction.TransactionResponse;
import com.example.MyBank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/bank")
public class BankController {

    @Autowired
    private BankService bankService;


    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> getCashAccountBalance() throws AccountBalanceNotFoundException {

        return ResponseEntity.ok(bankService.getCashAccountBalance());
    }

    @GetMapping("/transaction")
    public ResponseEntity<TransactionResponse> getCashAccountTransactions(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                              Date fromAccountingDate,@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Date toAccountingDate) throws TransactionException {
        return ResponseEntity.ok(bankService.getCashAccountTransactions(fromAccountingDate,toAccountingDate));
    }

    @PostMapping("/money-transfer")
    public ResponseEntity<MoneyTransferResponse> createMoneyTransfer(@RequestBody MoneyTransferRequest moneyTransferRequest) throws MoneyTransferException {
        return ResponseEntity.ok(bankService.createMoneyTransfer(moneyTransferRequest));
    }
}
