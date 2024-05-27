package com.example.MyBank.model;

import com.example.MyBank.enumeration.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AccountTransaction {

    private String transactionId;
    private String operationId;
    private Date accountingDate;
    private Date valueDate;
    private TransactionType type;
    private Number amount;
    private String currency;
    private String description;

}
