package com.example.MyBank.model.moneyTransfer.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
class Fee {
    private String feeCode;
    private String description;
    private double amount;
    private String currency;
}
