package com.example.MyBank.exception;

public class MoneyTransferException extends Exception {
    public MoneyTransferException(String code, String description) {
        super(String.format("Error code: %s, Description: %s", code, description));
    }
}
