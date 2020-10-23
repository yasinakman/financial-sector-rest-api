package com.akman.springbootdemo.rest_api.controller;

import com.akman.springbootdemo.model.customer.CustomerRequest;
import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.enums.AscOrDesc;
import com.akman.springbootdemo.model.enums.CustomerField;
import com.akman.springbootdemo.service.customerservice.CustomerService;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(PowerMockRunner.class)
public class CustomerControllerTest {

    private static final String CUSTOMER_ENDPOINT = "/api/v1/customer/";

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Mock
    private CustomerService customerService;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mvc = MockMvcBuilders.standaloneSetup(new CustomerController(customerService)).build();
    }

    @Test
    public void testSaveCustomer() throws Exception {
        CustomerRequest customerRequest = prepareCustomerRequest();
        String requestStr = objectMapper.writeValueAsString(customerRequest);
        given(customerService.saveCustomer(customerRequest)).willReturn(prepareCustomerResponse());

        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ENDPOINT + "save-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestStr))
                .andExpect(status().isCreated()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        CustomerResponse customerResponse = objectMapper.readValue(contentAsString, CustomerResponse.class);

        //verification
        assertEquals(customerRequest.getId(), customerResponse.getId());
    }

    @Test
    public void testListCustomersAndSortBy() throws Exception {
        final CustomerField rating = CustomerField.RATING;
        final AscOrDesc ascOrDesc = AscOrDesc.ASCENDING;
        CustomerRequest customerRequest = prepareCustomerRequest();
        given(customerService.listCustomers(Lists.newArrayList(customerRequest.getName()),
                Lists.newArrayList(customerRequest.getLastName()), rating, ascOrDesc))
                .willReturn(Lists.newArrayList(prepareCustomerResponse()));

        MvcResult mvcResult = mvc.perform(get(CUSTOMER_ENDPOINT + "list-customers")
                .contentType(MediaType.APPLICATION_JSON)
                .param("nameList", customerRequest.getName())
                .param("lastNameList", customerRequest.getLastName())
                .param("sortBy", rating.name())
                .param("ascOrDesc", ascOrDesc.name()))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<CustomerResponse> customerResponseList = objectMapper.readValue(contentAsString, new TypeReference<List<CustomerResponse>>() {
        });

        assertEquals(customerRequest.getId(), customerResponseList.get(0).getId());
    }

    private CustomerResponse prepareCustomerResponse() {
        return CustomerResponse.builder()
                .id(1L)
                .name("yasin")
                .lastName("akman")
                .address("Istanbul")
                .dayOfBirth(LocalDate.of(1996, 3, 12))
                .rating(1)
                .build();
    }

    private CustomerRequest prepareCustomerRequest() {
        return CustomerRequest.builder()
                .id(1L)
                .name("yasin")
                .lastName("akman")
                .dayOfBirth(LocalDate.of(1996, 3, 12))
                .address("Istanbul")
                .rating(1).build();
    }
}
