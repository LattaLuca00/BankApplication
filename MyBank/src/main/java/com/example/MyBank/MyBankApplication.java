package com.example.MyBank;

import com.example.MyBank.controller.BankController;
import com.example.MyBank.model.balance.BalanceResponse;
import com.example.MyBank.model.moneyTransfer.request.*;
import com.example.MyBank.model.moneyTransfer.response.MoneyTransferResponse;
import com.example.MyBank.model.transaction.TransactionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class MyBankApplication {

    public static void main(String[] args) throws JsonProcessingException, ParseException {
        ApplicationContext context = SpringApplication.run(MyBankApplication.class, args);

        BankController bankController = context.getBean(BankController.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Stampa il saldo corrente (prove effettuate anche su Postman)
        System.out.println("LETTURA SALDO");

        BalanceResponse balanceResponse = bankController.getCashAccountBalance("14537780");

        System.out.println(objectMapper.writeValueAsString(balanceResponse));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse("2023-01-01");
        Date toDate = dateFormat.parse("2023-01-15");

        // Stampa la lista delle transazioni (prove effettuate anche su Postman)
        System.out.println("LETTURA TRANSAZIONI");

        TransactionResponse transactionResponse = bankController.getCashAccountTransactions("14537780", fromDate, toDate);

        System.out.println(objectMapper.writeValueAsString(transactionResponse));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date executionDate = sdf.parse("2019-04-01");

        MoneyTransferRequest moneyTransferRequest = MoneyTransferRequest.builder()
                .creditor(
                        Creditor.builder()
                                .name("John Doe")
                                .account(CreditorAccount.builder()
                                        .accountCode("IT23A0336844430152923804660")
                                        .bicCode("SELBIT2BXXX").build())
                                .address(CreditorAddress.builder()
                                        .address(null)
                                        .city(null)
                                        .countryCode(null).build()).build()
                )
                .executionDate(executionDate)
                .uri("REMITTANCE_INFORMATION")
                .description("Payment invoice 75/2017")
                .amount(800)
                .currency("EUR")
                .isUrgent(false)
                .isInstant(false)
                .feeType("SHA")
                .feeAccountId("45685475")
                .taxRelief(TaxRelief.builder()
                        .taxReliefId("L449")
                        .isCondoUpgrade(false)
                        .creditorFiscalCode("56258745832")
                        .beneficiaryType("NATURAL_PERSON")
                        .naturalPersonBeneficiary(NaturalPersonBeneficiary.builder()
                                .fiscalCode1("MRLFNC81L04A859L")
                                .fiscalCode2(null)
                                .fiscalCode3(null)
                                .fiscalCode4(null)
                                .fiscalCode5(null).build())
                        .legalPersonBeneficiary(LegalPersonBeneficiary.builder()
                                .fiscalCode(null)
                                .legalRepresentativeFiscalCode(null).build()).build()).build();

        // Stampa errore per la creazione di un bonifico (prove fatte anche su postman con body di esempio preso da Fabrick)
        System.out.println("BONIFICO");

        MoneyTransferResponse moneyTransferResponse = bankController.createMoneyTransfer("14537780", moneyTransferRequest);

        System.out.println(objectMapper.writeValueAsString(moneyTransferResponse));

    }
}
