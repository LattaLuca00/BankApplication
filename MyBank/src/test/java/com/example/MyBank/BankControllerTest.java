package com.example.MyBank;

import com.example.MyBank.configuration.SandboxConfig;
import com.example.MyBank.controller.BankController;
import com.example.MyBank.exception.AccountBalanceNotFoundException;
import com.example.MyBank.exception.TransactionException;
import com.example.MyBank.model.balance.Balance;
import com.example.MyBank.model.balance.BalanceResponse;
import com.example.MyBank.model.transaction.Transaction;
import com.example.MyBank.model.transaction.TransactionResponse;
import com.example.MyBank.model.transaction.TransactionType;
import com.example.MyBank.service.BankService;
import org.junit.jupiter.api.Test;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BankController.class)
public class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BankService bankService;
    @MockBean
    private SandboxConfig sandboxConfig;

    @Test
    public void testGetCashAccountBalance() throws Exception {

        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setStatus("OK");
        balanceResponse.setPayload(Collections.singletonList(new Balance("2024-05-28", -28.09, -28.09, "EUR")));

        given(bankService.getCashAccountBalance()).willReturn(balanceResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/bank/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].date").value("2024-05-28"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].balance").value(-28.09))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].availableBalance").value(-28.09))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].currency").value("EUR"));

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].transactionId").value("286375735200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].operationId").value("23000004239582"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].accountingDate").value("2023-01-05"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].valueDate").value("2023-01-05"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].type.enumeration").value("GBS_TRANSACTION_TYPE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].type.value").value("GBS_ACCOUNT_TRANSACTION_TYPE_0009"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].amount").value(-0.10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].currency").value("EUR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].description").value("BA TERRIBILE LUCA        REC 94748B390EF241F7ABFADAF8588D9CEE TEST CUTOFF"));

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


  /*  public void testGetCashAccountTransactions() throws Exception {
        // Mock service response
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setStatus("OK");
        transactionResponse.setPayload(new Transaction[] { new Transaction("txn123", "op123", "2024-05-28", "2024-05-28",
                new TransactionType("ENUM1", "Value1"), 100.0, "EUR", "Transaction Description") });

        given(bankService.getCashAccountTransactions(any(Date.class), any(Date.class))).willReturn(transactionResponse);

        // Perform GET request and verify
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fromDate = dateFormat.format(new Date());
        String toDate = dateFormat.format(new Date());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/gbs/banking/v4.0/accounts/{accountId}/transactions", "your_account_id_here")
                        .param("fromAccountingDate", fromDate)
                        .param("toAccountingDate", toDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].transactionId").value("txn123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].operationId").value("op123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].accountingDate").value("2024-05-28"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].valueDate").value("2024-05-28"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].type.enumeration").value("ENUM1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].type.value").value("Value1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].amount").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].currency").value("EUR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].description").value("Transaction Description"));
    }*/

    // Add test for createMoneyTransfer here

}