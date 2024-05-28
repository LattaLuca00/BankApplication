package com.example.MyBank.exception;

public class AccountBalanceNotFoundException extends Exception {

    public AccountBalanceNotFoundException(String code, String description) {
        super(String.format("Error code: %s, Description: %s", code, description));
    }
}