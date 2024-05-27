package com.example.MyBank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Balance {

    private Date date;
    private Number balance;
    private Number availableBalance;
    private String currency;
}
