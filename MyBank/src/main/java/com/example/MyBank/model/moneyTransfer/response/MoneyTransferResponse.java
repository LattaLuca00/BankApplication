package com.example.MyBank.model.moneyTransfer.response;

import com.example.MyBank.model.ResponseError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MoneyTransferResponse {
    private String status;
    private ResponseError[] error;
    private List<MoneyTransfer> payload;


}
