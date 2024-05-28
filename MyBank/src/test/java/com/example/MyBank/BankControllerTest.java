package com.example.MyBank;

import com.example.MyBank.configuration.SandboxConfig;
import com.example.MyBank.controller.BankController;
import com.example.MyBank.exception.AccountBalanceNotFoundException;
import com.example.MyBank.exception.TransactionException;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BankController.class)
public class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BankService bankService;
    @MockBean
    private SandboxConfig sandboxConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetCashAccountBalance() throws Exception {

        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setStatus("OK");
        balanceResponse.setPayload(Collections.singletonList(new Balance("2024-05-28", -28.09, -28.09, "EUR")));

        given(bankService.getCashAccountBalance()).willReturn(balanceResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/bank/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.payload[0].date").value("2024-05-28"))
                .andExpect(jsonPath("$.payload[0].balance").value(-28.09))
                .andExpect(jsonPath("$.payload[0].availableBalance").value(-28.09))
                .andExpect(jsonPath("$.payload[0].currency").value("EUR"));

    }

    @Test
    public void testGetCashAccountBalance_AccountBalanceNotFoundException() throws Exception {

        given(sandboxConfig.getAccountId()).willReturn(99999999L);

        given(bankService.getCashAccountBalance()).willThrow(new AccountBalanceNotFoundException("REQ004", "Invalid account identifier"));
    }


    @Test
    public void testGetCashAccountTransactions() throws Exception {

        Transaction transaction = new Transaction(
                "286375735200",
                "23000004239582",
                "2023-01-05",
                "2023-01-05",
                new TransactionType("GBS_TRANSACTION_TYPE", "GBS_ACCOUNT_TRANSACTION_TYPE_0009"),
                -0.10,
                "EUR",
                "BA TERRIBILE LUCA        REC 94748B390EF241F7ABFADAF8588D9CEE TEST CUTOFF"
        );
        TransactionResponse mockResponse = new TransactionResponse();
        mockResponse.setStatus("OK");
        mockResponse.setPayload(List.of(transaction));

        given(bankService.getCashAccountTransactions(ArgumentMatchers.any(Date.class), ArgumentMatchers.any(Date.class)))
                .willReturn(mockResponse);


        mockMvc.perform(MockMvcRequestBuilders.get("/bank/transaction")
                        .param("fromAccountingDate", "2023-01-01")
                        .param("toAccountingDate", "2023-01-05")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.payload[0].transactionId").value("286375735200"))
                .andExpect(jsonPath("$.payload[0].operationId").value("23000004239582"))
                .andExpect(jsonPath("$.payload[0].accountingDate").value("2023-01-05"))
                .andExpect(jsonPath("$.payload[0].valueDate").value("2023-01-05"))
                .andExpect(jsonPath("$.payload[0].type.enumeration").value("GBS_TRANSACTION_TYPE"))
                .andExpect(jsonPath("$.payload[0].type.value").value("GBS_ACCOUNT_TRANSACTION_TYPE_0009"))
                .andExpect(jsonPath("$.payload[0].amount").value(-0.10))
                .andExpect(jsonPath("$.payload[0].currency").value("EUR"))
                .andExpect(jsonPath("$.payload[0].description").value("BA TERRIBILE LUCA        REC 94748B390EF241F7ABFADAF8588D9CEE TEST CUTOFF"));

    }

    @Test
    public void testGetCashAccountTransactions_TransactionsException() throws Exception {
        given(sandboxConfig.getAccountId()).willReturn(99999999L);

        given(bankService.getCashAccountTransactions(ArgumentMatchers.any(Date.class), ArgumentMatchers.any(Date.class)))
                .willThrow(new TransactionException("REQ004", "Invalid account identifier"));

    }

    @Test
    public void testGetCashAccountTransactions_noValidData_TransactionsNotFoundException() throws Exception {

        given(bankService.getCashAccountTransactions(ArgumentMatchers.any(Date.class), ArgumentMatchers.any(Date.class)))
                .willThrow(new TransactionException("REQ017", "Invalid date format"));

        // Perform the request with invalid date format
        mockMvc.perform(MockMvcRequestBuilders.get("/bank/transaction")
                        .param("fromAccountingDate", "invalid-date")
                        .param("toAccountingDate", "invalid-date")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TransactionException))
                .andExpect(result -> assertEquals("Invalid date format", result.getResolvedException().getMessage()));

    }

    @Test
    public void testMoneyTransfer_withError() throws Exception {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date executionDate = sdf.parse("2019-04-01");

        MoneyTransferRequest request = MoneyTransferRequest.builder()
                .creditor(Creditor.builder()
                        .name("John Doe")
                        .account(CreditorAccount.builder()
                                .accountCode("IT23A0336844430152923804660")
                                .bicCode("SELBIT2BXXX")
                                .build())
                        .address(CreditorAddress.builder()
                                .address(null)
                                .city(null)
                                .countryCode(null)
                                .build())
                        .build())
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
                                .fiscalCode5(null)
                                .build())
                        .legalPersonBeneficiary(LegalPersonBeneficiary.builder()
                                .fiscalCode(null)
                                .legalRepresentativeFiscalCode(null)
                                .build())
                        .build())
                .build();
        
        given(bankService.createMoneyTransfer(ArgumentMatchers.any(MoneyTransferRequest.class))).willThrow(new TransactionException("API000",
                "it.sella.pagamenti.servizibonifico.exception.ServiziInvioBonificoSubsystemException: " +
                        "it.sella.pagamenti.sottosistemi.SottosistemiException: Errore tecnico CONTO 45685475:Conto 45685475 non esiste"));

        mockMvc.perform(post("/bank/money-transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("KO"))
                .andExpect(jsonPath("$.errors[0].code").value("API000"))
                .andExpect(jsonPath("$.errors[0].description").value("it.sella.pagamenti.servizibonifico.exception.ServiziInvioBonificoSubsystemException: it.sella.pagamenti.sottosistemi.SottosistemiException: Errore tecnico CONTO 45685475:Conto 45685475 non esiste"));
    }

}