package com.example.MyBank.model.moneyTransfer.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DebtorAccount {
    private String accountCode;
    private String bicCode;
}
