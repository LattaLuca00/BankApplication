package com.example.MyBank.model.moneyTransfer.response;

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
public class MoneyTransferResponse {
    private String status;
    @JsonAlias({"errors", "error"})
    private ResponseError[] error;
    private MoneyTransferPayload payload;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class MoneyTransferPayload {
        private List<MoneyTransfer> list;
    }
}
