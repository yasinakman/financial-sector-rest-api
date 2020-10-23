package com.akman.springbootdemo.rest_api.controller;

import com.akman.springbootdemo.model.account.AccountRequest;
import com.akman.springbootdemo.model.account.AccountResponse;
import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.service.accountservice.AccountService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(PowerMockRunner.class)
public class AccountControllerTest {

    private static final String ACCOUNT_ENDPOINT = "/api/v1/account/";

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Mock
    private AccountService accountService;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mvc = MockMvcBuilders.standaloneSetup(new AccountController(accountService)).build();
    }

    @Test
    public void testSaveCustomer() throws Exception {
        //preparation
        AccountRequest accountRequest = prepareAccountRequest();
        String requestStr = objectMapper.writeValueAsString(accountRequest);
        given(accountService.saveAccount(accountRequest)).willReturn(prepareAccountResponse());

        //test
        MvcResult mvcResult = mvc.perform(post(ACCOUNT_ENDPOINT + "save-account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestStr))
                .andExpect(status().isCreated()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        AccountResponse accountResponse = objectMapper.readValue(contentAsString, AccountResponse.class);

        //verification
        assertEquals(accountRequest.getId(), accountResponse.getId());
        assertEquals(accountRequest.getCustomer(), accountResponse.getCustomer().getId());
    }

    @Test
    public void testListAccountsByCustomer() throws Exception {
        //preparation
        AccountRequest accountRequest = prepareAccountRequest();
        given(accountService.listAccountsByCustomer(accountRequest.getId()))
                .willReturn(Lists.newArrayList(prepareAccountResponse()));

        //test
        MvcResult mvcResult = mvc.perform(get(ACCOUNT_ENDPOINT + "list-accounts-by-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", accountRequest.getId().toString()))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<AccountResponse> accountResponseList = objectMapper.readValue(contentAsString, new TypeReference<List<AccountResponse>>() {
        });

        //verification
        assertEquals(accountRequest.getId(), accountResponseList.get(0).getId());
    }

    private AccountResponse prepareAccountResponse() {
        return AccountResponse.builder()
                .id(1L)
                .balance(BigDecimal.TEN)
                .no(123546L)
                .currency(Currency.TRY)
                .customer(prepareCustomerResponse())
                .build();
    }

    private CustomerResponse prepareCustomerResponse() {
        return CustomerResponse.builder()
                .id(1L).build();
    }

    private AccountRequest prepareAccountRequest() {
        return AccountRequest.builder()
                .id(1L)
                .balance(BigDecimal.TEN)
                .no(123546L)
                .currency(Currency.TRY)
                .customer(prepareCustomerResponse().getId())
                .build();
    }
}