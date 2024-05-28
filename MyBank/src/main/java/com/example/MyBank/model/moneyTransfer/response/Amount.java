package com.example.MyBank.model.moneyTransfer.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Amount {
    private double debtorAmount;
    private String debtorCurrency;
    private double creditorAmount;
    private String creditorCurrency;
    private Date creditorCurrencyDate;
    private double exchangeRate;
}
