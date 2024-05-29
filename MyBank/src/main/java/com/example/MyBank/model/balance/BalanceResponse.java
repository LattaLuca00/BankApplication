package com.example.MyBank.model.balance;

import com.example.MyBank.model.ResponseError;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BalanceResponse {
    private String status;
    @JsonAlias({"errors", "error"})
    private ResponseError[] error;
    private Balance payload;

}