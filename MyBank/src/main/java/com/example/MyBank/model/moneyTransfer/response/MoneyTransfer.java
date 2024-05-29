package com.example.MyBank.model.moneyTransfer.response;

import com.example.MyBank.model.moneyTransfer.request.Creditor;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MoneyTransfer {
    private String moneyTransferId;
    private String status;
    private String direction;
    private Creditor creditor;
    private Debtor debtor;
    private String cro;
    private String trn;
    private String uri;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Europe/Rome")
    private Date createdDatetime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Europe/Rome")
    private Date accountedDatetime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date debtorValueDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date creditorValueDate;
    private Amount amount;
    private boolean isUrgent;
    private boolean isInstant;
    private String feeType;
    private String feeAccountId;
    private Fee[] fees;
    private boolean hasTaxRelief;
}
