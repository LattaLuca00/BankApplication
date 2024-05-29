package com.example.MyBank.model.transaction;


import com.example.MyBank.model.ResponseError;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TransactionResponse {
    private String status;
    @JsonAlias({"errors", "error"})
    private ResponseError[] error;
    private Payload payload;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class Payload {
        private List<Transaction> list;
    }
}

