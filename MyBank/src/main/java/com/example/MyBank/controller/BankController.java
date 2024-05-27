package com.example.MyBank.controller;

import com.example.MyBank.model.Balance;
import com.example.MyBank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @GetMapping("/get-saldo")
    public ResponseEntity<Balance> getSaldo(){

        return ResponseEntity.ok(bankService.getSaldo());
    }
}
