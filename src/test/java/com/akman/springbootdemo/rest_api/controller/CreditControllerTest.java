package com.akman.springbootdemo.rest_api.controller;

import com.akman.springbootdemo.model.credit.CreditRequest;
import com.akman.springbootdemo.model.credit.CreditResponse;
import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.service.creditservice.CreditService;
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
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(PowerMockRunner.class)
public class CreditControllerTest {

    private static final String CREDIT_ENDPOINT = "/api/v1/credit/";

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Mock
    private CreditService creditService;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mvc = MockMvcBuilders.standaloneSetup(new CreditController(creditService)).build();
    }

    @Test
    public void testSaveCredit() throws Exception {
        //preparation
        CreditRequest creditRequest = prepareCreditRequest();
        String requestStr = objectMapper.writeValueAsString(creditRequest);
        given(creditService.saveCredit(creditRequest)).willReturn(prepareCreditResponse());

        //test
        MvcResult mvcResult = mvc.perform(post(CREDIT_ENDPOINT + "save-credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestStr))
                .andExpect(status().isCreated()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        CreditResponse creditResponse = objectMapper.readValue(contentAsString, CreditResponse.class);

        //verification
        assertEquals(creditRequest.getId(), creditResponse.getId());
        assertEquals(creditRequest.getCustomer(), creditResponse.getCustomer().getId());
    }

    @Test
    public void testListCredits() throws Exception {
        //preparation
        CreditRequest creditRequest = prepareCreditRequest();
        given(creditService.listCreditsByCustomer(creditRequest.getId(), false))
                .willReturn(Lists.newArrayList(prepareCreditResponse()));

        //test
        MvcResult mvcResult = mvc.perform(get(CREDIT_ENDPOINT + "list-credits")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", creditRequest.getId().toString())
                .param("isExceeded", "false"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<CreditResponse> creditResponseList = objectMapper.readValue(contentAsString, new TypeReference<List<CreditResponse>>() {
        });

        //verification
        assertEquals(creditRequest.getId(), creditResponseList.get(0).getId());
    }

    private CreditResponse prepareCreditResponse() {
        return CreditResponse.builder()
                .id(1L)
                .createdDateTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .currency(Currency.TRY)
                .customer(prepareCustomerResponse())
                .isActive(true)
                .creditAmount(BigDecimal.TEN)
                .remainingCreditAmount(BigDecimal.ONE)
                .remainingTerm(5L)
                .term(10L)
                .build();
    }

    private CustomerResponse prepareCustomerResponse() {
        return CustomerResponse.builder()
                .id(2L).build();
    }

    private CreditRequest prepareCreditRequest() {
        return CreditRequest.builder()
                .id(1L)
                .customer(prepareCustomerResponse().getId())
                .currency(Currency.TRY)
                .creditAmount(BigDecimal.TEN)
                .term(10L)
                .build();
    }
}