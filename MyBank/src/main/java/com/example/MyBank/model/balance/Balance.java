package com.example.MyBank.model.balance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Balance {
    private String date;
    private double balance;
    private double availableBalance;
    private String currency;

}
