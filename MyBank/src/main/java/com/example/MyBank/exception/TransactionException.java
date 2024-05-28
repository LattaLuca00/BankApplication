package com.example.MyBank.exception;

public class TransactionException extends Exception {
    public TransactionException(String code, String description) {
        super(String.format("Error code: %s, Description: %s", code, description));
    }
}
