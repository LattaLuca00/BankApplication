package com.example.MyBank.model.moneyTransfer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LegalPersonBeneficiary {
    private String fiscalCode;
    private String legalRepresentativeFiscalCode;
}
