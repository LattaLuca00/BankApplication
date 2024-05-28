package com.example.MyBank.model.moneyTransfer.response;

import com.example.MyBank.model.moneyTransfer.request.Creditor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
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
    private LocalDateTime createdDatetime;
    private LocalDateTime accountedDatetime;
    private Date debtorValueDate;
    private Date creditorValueDate;
    private Amount amount;
    private boolean isUrgent;
    private boolean isInstant;
    private String feeType;
    private String feeAccountId;
    private Fee[] fees;
    private boolean hasTaxRelief;
}
