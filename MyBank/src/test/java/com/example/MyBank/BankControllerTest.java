package com.example.MyBank;

import com.example.MyBank.controller.BankController;
import com.example.MyBank.model.ResponseError;
import com.example.MyBank.model.balance.Balance;
import com.example.MyBank.model.balance.BalanceResponse;
import com.example.MyBank.model.moneyTransfer.request.*;
import com.example.MyBank.model.moneyTransfer.response.MoneyTransferResponse;
import com.example.MyBank.model.transaction.Transaction;
import com.example.MyBank.model.transaction.TransactionResponse;
import com.example.MyBank.model.transaction.TransactionType;
import com.example.MyBank.service.BankService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BankController.class)
public class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankService bankService;

    @Autowired
    private ObjectMapper objectMapper;

    private BalanceResponse balanceResponse;
    private TransactionResponse transactionResponse;
    private MoneyTransferResponse moneyTransferResponse;
    private MoneyTransferRequest moneyTransferRequest;

    private String accountId;

    @BeforeEach
    public void setup() throws Exception {

        accountId = "14537780";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date executionDate = dateFormat.parse("2019-03-31");
        String balanceDate = dateFormat.format(new Date());

        balanceResponse = BalanceResponse.builder()
                .status("OK")
                .error(new ResponseError[0])
                .payload(Balance.builder()
                        .date(balanceDate)
                        .balance(-28.09)
                        .availableBalance(-28.09)
                        .currency("EUR")
                        .build())
                .build();

        transactionResponse = TransactionResponse.builder()
                .status("OK")
                .error(new ResponseError[0])
                .payload(TransactionResponse.Payload.builder().list(Collections.singletonList(
                        Transaction.builder()
                                .transactionId("286375735200")
                                .operationId("23000004239582")
                                .accountingDate(executionDate)
                                .valueDate(executionDate)
                                .type(TransactionType.builder().enumeration("GBS_TRANSACTION_TYPE")
                                        .value("GBS_ACCOUNT_TRANSACTION_TYPE_0009")
                                        .build())
                                .amount(-0.10)
                                .currency("EUR")
                                .description("BA TERRIBILE LUCA        REC 94748B390EF241F7ABFADAF8588D9CEE TEST CUTOFF")
                                .build()
                )).build())
                .build();

        moneyTransferRequest = MoneyTransferRequest.builder()
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

        ResponseError[] responseErrors = {(ResponseError.builder().code("API000")
                .description("it.sella.pagamenti.servizibonifico.exception.ServiziInvioBonificoSubsystemException: " +
                        "it.sella.pagamenti.sottosistemi.SottosistemiException: Errore tecnico CONTO 45685475:Conto 45685475 non esiste")
                .build())};
        moneyTransferResponse = MoneyTransferResponse.builder()
                .status("KO")
                .error(responseErrors).build();
    }

    @Test
    public void testGetCashAccountBalance() throws Exception {
        when(bankService.getCashAccountBalance(anyString())).thenReturn(balanceResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/bank/balance/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.payload.balance").value(-28.09))
                .andExpect(jsonPath("$.payload.availableBalance").value(-28.09))
                .andExpect(jsonPath("$.payload.currency").value("EUR"));
    }

    @Test
    public void testGetCashAccountTransactions() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateString = dateFormat.format(dateFormat.parse("2023-01-01"));
        String toDateString = dateFormat.format(dateFormat.parse("2023-01-15"));

        when(bankService.getCashAccountTransactions(anyString(), any(Date.class), any(Date.class))).thenReturn(transactionResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/bank/transaction/" + accountId)
                        .param("fromAccountingDate", fromDateString)
                        .param("toAccountingDate", toDateString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").isEmpty())
                .andExpect(jsonPath("$.payload.list[0].transactionId").value("286375735200"))
                .andExpect(jsonPath("$.payload.list[0].operationId").value("23000004239582"))
                .andExpect(jsonPath("$.payload.list[0].accountingDate").value("2019-03-30"))
                .andExpect(jsonPath("$.payload.list[0].valueDate").value("2019-03-30"))
                .andExpect(jsonPath("$.payload.list[0].type.enumeration").value("GBS_TRANSACTION_TYPE"))
                .andExpect(jsonPath("$.payload.list[0].type.value").value("GBS_ACCOUNT_TRANSACTION_TYPE_0009"))
                .andExpect(jsonPath("$.payload.list[0].amount").value(-0.10))
                .andExpect(jsonPath("$.payload.list[0].currency").value("EUR"));
    }

    @Test
    public void testCreateMoneyTransfer() throws Exception {
        when(bankService.createMoneyTransfer(anyString(), any(MoneyTransferRequest.class))).thenReturn(moneyTransferResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/bank/money-transfer/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(moneyTransferRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("KO"))
                .andExpect(jsonPath("$.error[0].code").value("API000"))
                .andExpect(jsonPath("$.error[0].description").value("it.sella.pagamenti.servizibonifico.exception.ServiziInvioBonificoSubsystemException: "+
                                                "it.sella.pagamenti.sottosistemi.SottosistemiException: Errore tecnico CONTO 45685475:Conto 45685475 non esiste"));
    }
}
