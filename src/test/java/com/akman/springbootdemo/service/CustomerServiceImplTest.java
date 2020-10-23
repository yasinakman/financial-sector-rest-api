package com.akman.springbootdemo.service;

import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.customer.CustomerRequest;
import com.akman.springbootdemo.model.enums.AscOrDesc;
import com.akman.springbootdemo.model.enums.CustomerField;
import com.akman.springbootdemo.repository.CustomerRepository;
import com.akman.springbootdemo.rest_api.exceptions.BadRequestException;
import com.akman.springbootdemo.service.customerservice.CustomerServiceImpl;
import com.akman.springbootdemo.utils.ErrorConstants;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CustomerServiceImpl.class)
public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void testSaveCustomer() {
        //preparation
        CustomerRequest customerRequest = prepareCustomerRequest();
        customerRequest.setId(null);

        //test
        customerService.saveCustomer(customerRequest);

        //verification
        verify(customerRepository, times(1)).save(any());
    }

    @Test
    public void testSaveCustomer_ratingLessThan1_exception() {
        //preparation
        CustomerRequest customerRequest = prepareCustomerRequest();
        customerRequest.setRating(0);

        //test
        try {
            customerService.saveCustomer(customerRequest);
            fail("exception has not thrown");
        } catch (Exception e) {
            assertEquals(ErrorConstants.RATING_CAN_NOT_BE_LESS_THAN_1_AND_GREATER_THAN_4, e.getMessage());
            assertEquals(BadRequestException.class, e.getClass());
        }

        //verification
        verify(customerRepository, times(0)).existsById(customerRequest.getId());
    }

    @Test
    public void testSaveCustomer_withIdThatNotExistsInRepository_exception() {
        //preparation
        CustomerRequest customerRequest = prepareCustomerRequest();
        doReturn(false).when(customerRepository).existsById(customerRequest.getId());

        //test
        try {
            customerService.saveCustomer(customerRequest);
            fail("exception has not thrown");
        } catch (Exception e) {
            assertEquals(ErrorConstants.CUSTOMER_SAVE_FAILED, e.getMessage());
            assertEquals(BadRequestException.class, e.getClass());
        }

        //verification
        verify(customerRepository, times(1)).existsById(customerRequest.getId());
    }

    @Test
    public void testSaveCustomer_exception() {
        //preparation
        CustomerRequest customerRequest = prepareCustomerRequest();
        customerRequest.setId(null);
        doThrow(new NullPointerException()).when(customerRepository).save(any());

        //test
        try {
            customerService.saveCustomer(customerRequest);
            fail("exception has not thrown");
        } catch (Exception e) {
            assertEquals(ErrorConstants.CUSTOMER_SAVE_FAILED, e.getMessage());
            assertEquals(BadRequestException.class, e.getClass());
        }

        //verification
        verify(customerRepository, times(1)).save(any());
    }

    @Test
    public void testListCustomers() {
        //preparation
        Customer customer = prepareCustomer();
        CustomerField rating = CustomerField.RATING;
        doReturn(Lists.newArrayList(customer)).when(customerRepository)
                .findAllByNameAndLastNameIn(null, null, Sort.by(rating.getValue()).ascending());

        //test
        customerService.listCustomers(null, null, rating, AscOrDesc.ASCENDING);

        //verification
        verify(customerRepository, times(1))
                .findAllByNameAndLastNameIn(null, null, Sort.by(rating.getValue()).ascending());
    }

    private CustomerRequest prepareCustomerRequest() {
        return CustomerRequest.builder()
                .id(1L)
                .name("yasin")
                .lastName("akman")
                .dayOfBirth(LocalDate.of(2020, 3, 12))
                .address("Istanbul")
                .rating(2).build();
    }

    private Customer prepareCustomer() {
        return Customer.builder()
                .id(1L)
                .name("yasin")
                .lastName("akman")
                .dayOfBirth(LocalDate.of(2020, 3, 12))
                .address("Istanbul")
                .rating(2).build();
    }
}
