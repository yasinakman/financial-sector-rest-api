package com.akman.springbootdemo.rest_api.controller;

import com.akman.springbootdemo.model.account.AccountResponse;
import com.akman.springbootdemo.model.credit.CreditResponse;
import com.akman.springbootdemo.model.customer.CustomerRequest;
import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.enums.*;
import com.akman.springbootdemo.model.transaction.TransactionRequest;
import com.akman.springbootdemo.model.transaction.TransactionResponse;
import com.akman.springbootdemo.service.transactionservice.TransactionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(PowerMockRunner.class)
public class TransactionControllerTest {

    private static final String TRANSACTION_ENDPOINT = "/api/v1/transaction/";

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Mock
    private TransactionService transactionService;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mvc = MockMvcBuilders.standaloneSetup(new TransactionController(transactionService)).build();
    }

    @Test
    public void testSaveCustomer() throws Exception {
        TransactionRequest transactionRequest = prepareTransactionRequest();
        String requestStr = objectMapper.writeValueAsString(transactionRequest);
        given(transactionService.saveTransaction(transactionRequest)).willReturn(prepareTransactionResponse());

        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ENDPOINT + "save-transaction-or-credit-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestStr))
                .andExpect(status().isCreated()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        TransactionResponse transactionResponse = objectMapper.readValue(contentAsString, TransactionResponse.class);

        //verification
        assertEquals(transactionRequest.getFromAccount(), transactionResponse.getFromAccount().getId());
        assertEquals(transactionRequest.getToAccount(), transactionResponse.getToAccount().getId());
        assertEquals(transactionRequest.getTotal(), transactionResponse.getTotal());
    }

    @Test
    public void testListCustomersAndSortBy() throws Exception {
        TransactionRequest transactionRequest = prepareTransactionRequest();
        given(transactionService.listOutgoingTransactionsByAccount(prepareAccountResponse().getId()))
                .willReturn(Lists.newArrayList(prepareTransactionResponse()));

        MvcResult mvcResult = mvc.perform(get(TRANSACTION_ENDPOINT + "list-transactions-by-account")
                .contentType(MediaType.APPLICATION_JSON)
                .param("accountId", transactionRequest.getFromAccount().toString())
                .param("transactionType", TransactionType.valueOf("OUTGOING").name()))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<TransactionResponse> transactionResponseList =
                objectMapper.readValue(contentAsString, new TypeReference<List<TransactionResponse>>() {
        });
        verify(transactionService, times(1))
                .listOutgoingTransactionsByAccount(prepareAccountResponse().getId());
        assertEquals(transactionRequest.getFromAccount(), transactionResponseList.get(0).getFromAccount().getId());
        assertEquals(transactionRequest.getToAccount(), transactionResponseList.get(0).getToAccount().getId());
        assertEquals(transactionRequest.getTotal(), transactionResponseList.get(0).getTotal());
    }

    @Test
    public void testListTransactionsByAccount() throws Exception {
        TransactionRequest transactionRequest = prepareTransactionCreditRequest();
        given(transactionService.listTransactionsByCredit(transactionRequest.getToCredit()))
                .willReturn(Lists.newArrayList(prepareTransactionCreditResponse()));

        MvcResult mvcResult = mvc.perform(get(TRANSACTION_ENDPOINT + "list-transactions-by-credit")
                .contentType(MediaType.APPLICATION_JSON)
                .param("creditId", transactionRequest.getToCredit().toString()))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<TransactionResponse> transactionResponseList =
                objectMapper.readValue(contentAsString, new TypeReference<List<TransactionResponse>>() {
        });
        verify(transactionService, times(1))
                .listTransactionsByCredit(transactionRequest.getToCredit());
        assertEquals(transactionRequest.getFromAccount(), transactionResponseList.get(0).getFromAccount().getId());
        assertEquals(transactionRequest.getToCredit(), transactionResponseList.get(0).getToCredit().getId());
        assertEquals(transactionRequest.getTotal(), transactionResponseList.get(0).getTotal());
    }

    @Test
    public void testListAllTransactions_page() throws Exception {
        TransactionField transactionTime = TransactionField.TRANSACTION_TIME;
        AscOrDesc sort = AscOrDesc.DESCENDING;
        TransactionResponse transactionResponse = prepareTransactionResponse();

        given(transactionService.listAllTransactionsPageable(null, null, transactionTime,
                sort, null, null, null, null))
                .willReturn(getPageable(Lists.newArrayList(transactionResponse)));

        mvc.perform(get(TRANSACTION_ENDPOINT + "list-all-transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .param("sortBy", transactionTime.name())
                .param("ascOrDesc", sort.name())
                .param("isPage", "true"))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).listAllTransactionsPageable(null,
                null, transactionTime, sort, null, null, null, null);
    }

    @Test
    public void testListAllTransactions_list() throws Exception {
        TransactionRequest transactionRequest = prepareTransactionRequest();
        TransactionField transactionTime = TransactionField.TRANSACTION_TIME;
        AscOrDesc sort = AscOrDesc.DESCENDING;
        TransactionResponse transactionResponse = prepareTransactionResponse();

        given(transactionService.listAllTransactions(null, null, transactionTime,
                sort, null, null))
                .willReturn(Lists.newArrayList(transactionResponse));

        MvcResult mvcResult = mvc.perform(get(TRANSACTION_ENDPOINT + "list-all-transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .param("sortBy", transactionTime.name())
                .param("ascOrDesc", sort.name())
                .param("isPage", "false"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<TransactionResponse> transactionResponseList =
                objectMapper.readValue(contentAsString, new TypeReference<List<TransactionResponse>>() {
        });
        verify(transactionService, times(1)).listAllTransactions(null, null,
                transactionTime, sort, null, null);
        assertEquals(transactionRequest.getFromAccount(), transactionResponseList.get(0).getFromAccount().getId());
        assertEquals(transactionRequest.getToAccount(), transactionResponseList.get(0).getToAccount().getId());
        assertEquals(transactionRequest.getTotal(), transactionResponseList.get(0).getTotal());
    }

    private <T> PageImpl<T> getPageable(List<T> list) {
        return new PageImpl<>(list);
    }

    private TransactionResponse prepareTransactionCreditResponse() {
        return TransactionResponse.builder()
                .id(1L)
                .fromAccount(prepareAccountResponse())
                .toCredit(prepareCreditResponse())
                .currency(Currency.TRY)
                .isCreditPayment(true)
                .total(BigDecimal.ONE)
                .transactionTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .build();
    }

    private TransactionResponse prepareTransactionResponse() {
        return TransactionResponse.builder()
                .id(1L)
                .fromAccount(prepareAccountResponse())
                .toAccount(prepareAccountResponse2())
                .currency(Currency.TRY)
                .isCreditPayment(false)
                .total(BigDecimal.ONE)
                .transactionTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .build();
    }

    private AccountResponse prepareAccountResponse() {
        return AccountResponse.builder()
                .id(2L)
                .balance(BigDecimal.TEN)
                .currency(Currency.TRY)
                .customer(prepareCustomerResponse())
                .no(123546L)
                .createdDateTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .build();
    }

    private AccountResponse prepareAccountResponse2() {
        return AccountResponse.builder()
                .id(5L)
                .balance(BigDecimal.TEN)
                .currency(Currency.TRY)
                .customer(prepareCustomerResponse())
                .no(654321L)
                .createdDateTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .build();
    }

    private CreditResponse prepareCreditResponse() {
        return CreditResponse.builder()
                .id(1L)
                .term(10L)
                .remainingTerm(5L)
                .creditAmount(BigDecimal.TEN)
                .remainingCreditAmount(BigDecimal.ONE)
                .isActive(true)
                .customer(prepareCustomerResponse())
                .currency(Currency.TRY)
                .createdDateTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .build();
    }

    private CustomerResponse prepareCustomerResponse() {
        return CustomerResponse.builder()
                .id(4L)
                .rating(2)
                .dayOfBirth(LocalDate.of(2020, 3, 12))
                .address("Istanbul")
                .lastName("akman")
                .name("yasin")
                .build();
    }

    private TransactionRequest prepareTransactionRequest() {
        return TransactionRequest.builder()
                .fromAccount(prepareAccountResponse().getId())
                .toAccount(prepareAccountResponse2().getId())
                .total(BigDecimal.ONE)
                .currency(Currency.TRY)
                .isCreditPayment(false)
                .build();
    }

    private TransactionRequest prepareTransactionCreditRequest() {
        return TransactionRequest.builder()
                .fromAccount(prepareAccountResponse().getId())
                .toCredit(prepareCreditResponse().getId())
                .total(BigDecimal.ONE)
                .currency(Currency.TRY)
                .isCreditPayment(false)
                .build();
    }
}
