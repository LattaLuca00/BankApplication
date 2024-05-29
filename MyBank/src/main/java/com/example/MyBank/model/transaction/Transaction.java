package com.example.MyBank.model.transaction;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Transaction {
    private String transactionId;
    private String operationId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accountingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date valueDate;

    private TransactionType type;
    private double amount;
    private String currency;
    private String description;
}
