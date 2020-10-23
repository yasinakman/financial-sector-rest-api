package com.akman.springbootdemo.service;

import com.akman.springbootdemo.model.account.Account;
import com.akman.springbootdemo.model.account.AccountRequest;
import com.akman.springbootdemo.model.credit.Credit;
import com.akman.springbootdemo.model.credit.CreditRequest;
import com.akman.springbootdemo.model.credit.CreditResponse;
import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.customer.CustomerRequest;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.repository.AccountRepository;
import com.akman.springbootdemo.repository.CreditRepository;
import com.akman.springbootdemo.repository.CustomerRepository;
import com.akman.springbootdemo.rest_api.exceptions.BadRequestException;
import com.akman.springbootdemo.service.accountservice.AccountServiceImpl;
import com.akman.springbootdemo.service.creditservice.CreditServiceImpl;
import com.akman.springbootdemo.service.customerservice.CustomerService;
import com.akman.springbootdemo.utils.ErrorConstants;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AccountServiceImpl.class)
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerService customerService;

    @Test
    public void testSaveAccount() {
        //preparation
        AccountRequest accountRequest = prepareAccountRequest();
        accountRequest.setId(null);
        doReturn(prepareCustomer()).when(customerService).getById(accountRequest.getCustomer());
        doReturn(prepareAccount()).when(accountRepository).save(any());

        //test
        accountService.saveAccount(accountRequest);

        //verification
        verify(customerService, times(1)).getById(accountRequest.getCustomer());
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    public void testSaveAccount_accountIdIsNull_exception() {
        //preparation
        AccountRequest accountRequest = prepareAccountRequest();
        doReturn(false).when(accountRepository).existsById(accountRequest.getId());

        //test and verification
        try {
            accountService.saveAccount(accountRequest);
            fail("exception not thrown");
        } catch (Exception e) {
            assertEquals(BadRequestException.class, e.getClass());
            assertEquals(ErrorConstants.COULD_NOT_FOUND_ANY_ACCOUNT_ID_TO_UPDATE, e.getMessage());
            verify(accountRepository, times(1)).existsById(accountRequest.getId());
        }
    }

    @Test
    public void testSaveAccount_exception() {
        //preparation
        AccountRequest accountRequest = prepareAccountRequest();
        doReturn(true).when(accountRepository).existsById(accountRequest.getId());
        doThrow(new NullPointerException()).when(customerService).getById(accountRequest.getCustomer());

        //test and verification
        try {
            accountService.saveAccount(accountRequest);
            fail("exception not thrown");
        } catch (Exception e) {
            assertEquals(BadRequestException.class, e.getClass());
            assertEquals(ErrorConstants.ACCOUNT_SAVE_FAILED, e.getMessage());
            verify(accountRepository, times(1)).existsById(accountRequest.getId());
            verify(customerService, times(1)).getById(accountRequest.getCustomer());
        }
    }

    @Test
    public void testListAccountsByCustomer() {
        //preparation
        AccountRequest accountRequest = prepareAccountRequest();
        doReturn(Lists.newArrayList(prepareAccount())).when(accountRepository)
                .findAllByCustomer_IdOrderByCreatedDateTimeDesc(accountRequest.getCustomer());

        //test
        accountService.listAccountsByCustomer(accountRequest.getCustomer());

        //verification
        verify(accountRepository, times(1))
                .findAllByCustomer_IdOrderByCreatedDateTimeDesc(accountRequest.getCustomer());
    }

    @Test
    public void testGetById() {
        //preparation
        AccountRequest accountRequest = prepareAccountRequest();
        doReturn(Optional.of(prepareAccount())).when(accountRepository).findById(accountRequest.getId());

        //test
        accountService.getById(accountRequest.getId());

        //verification
        verify(accountRepository, times(1)).findById(accountRequest.getId());
    }

    @Test
    public void testGetById_exception() {
        //preparation
        AccountRequest accountRequest = prepareAccountRequest();
        doReturn(Optional.empty()).when(accountRepository).findById(accountRequest.getId());

        //test
        try {
            accountService.getById(accountRequest.getId());
            fail("exception not thrown");
        } catch (Exception e) {
            assertEquals(BadRequestException.class, e.getClass());
            assertEquals(ErrorConstants.ACCOUNT_NOT_FOUND, e.getMessage());
        }

        //verification
        verify(accountRepository, times(1)).findById(accountRequest.getId());
    }

    private Customer prepareCustomer() {
        return Customer.builder()
                .id(1L)
                .name("yasin")
                .lastName("akman")
                .dayOfBirth(LocalDate.of(1996, 3, 12))
                .address("Istanbul")
                .rating(2)
                .build();
    }

    private Account prepareAccount() {
        return Account.builder()
                .id(1L)
                .customer(prepareCustomer())
                .currency(Currency.TRY)
                .no(123456L)
                .balance(BigDecimal.TEN)
                .build();
    }

    private AccountRequest prepareAccountRequest() {
        return AccountRequest.builder()
                .id(1L)
                .customer(prepareCustomer().getId())
                .currency(Currency.TRY)
                .no(123456L)
                .balance(BigDecimal.TEN)
                .build();
    }
}