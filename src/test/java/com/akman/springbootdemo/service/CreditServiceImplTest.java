package com.akman.springbootdemo.service;

import com.akman.springbootdemo.model.account.Account;
import com.akman.springbootdemo.model.credit.Credit;
import com.akman.springbootdemo.model.credit.CreditRequest;
import com.akman.springbootdemo.model.credit.CreditResponse;
import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.customer.CustomerRequest;
import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.enums.AscOrDesc;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.model.enums.CustomerField;
import com.akman.springbootdemo.repository.CreditRepository;
import com.akman.springbootdemo.repository.CustomerRepository;
import com.akman.springbootdemo.rest_api.exceptions.BadRequestException;
import com.akman.springbootdemo.service.creditservice.CreditServiceImpl;
import com.akman.springbootdemo.service.customerservice.CustomerService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CreditServiceImpl.class)
public class CreditServiceImplTest {

    @InjectMocks
    private CreditServiceImpl creditService;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CustomerService customerService;

    @Test
    public void testSaveCredit() {
        //preparation
        Credit credit = prepareCredit();
        CreditRequest creditRequest = prepareCreditRequest();
        creditRequest.setId(null);
        doReturn(prepareCustomer()).when(customerService).getById(creditRequest.getCustomer());
        doReturn(credit).when(creditRepository).save(any());

        //test
        creditService.saveCredit(creditRequest);

        //verification
        verify(creditRepository, times(0)).existsById(creditRequest.getId());
        verify(customerService, times(1)).getById(creditRequest.getCustomer());
        verify(creditRepository, times(1)).save(any());
    }

    @Test
    public void testSaveCredit_anyCreditIdFoundToUpdate_exception() {
        //preparation
        Credit credit = prepareCredit();
        CreditRequest creditRequest = prepareCreditRequest();
        doReturn(false).when(creditRepository).existsById(creditRequest.getId());
        doReturn(prepareCustomer()).when(customerService).getById(creditRequest.getCustomer());
        doReturn(credit).when(creditRepository).save(any());

        //test and verification
        try {
            creditService.saveCredit(creditRequest);
            fail("exception has not thrown");
        } catch (Exception e) {
            assertEquals(ErrorConstants.COULD_NOT_FOUND_ANY_CREDIT_TO_UPDATE, e.getMessage());
            assertEquals(BadRequestException.class, e.getClass());
            verify(creditRepository, times(1)).existsById(creditRequest.getId());
        }
    }

    @Test
    public void testSaveCredit_customerHasAnActiveCredit_exception() {
        //preparation
        CreditRequest creditRequest = prepareCreditRequest();
        Customer customer = prepareCustomer();
        customer.setCreditList(Lists.newArrayList(prepareCredit()));
        doReturn(true).when(creditRepository).existsById(creditRequest.getId());
        doReturn(customer).when(customerService).getById(creditRequest.getCustomer());

        //test and verification
        try {
            creditService.saveCredit(creditRequest);
            fail("exception has not thrown");
        } catch (Exception e) {
            assertEquals(ErrorConstants.CUSTOMER_HAS_AN_ACTIVE_CREDIT, e.getMessage());
            assertEquals(BadRequestException.class, e.getClass());
            verify(creditRepository, times(1)).existsById(creditRequest.getId());
            verify(customerService, times(1)).getById(creditRequest.getCustomer());
        }
    }

    @Test
    public void testSaveCredit_currencyNotCompatible_exception() {
        //preparation
        CreditRequest creditRequest = prepareCreditRequest();
        Customer customer = prepareCustomer();
        customer.setAccountList(Lists.newArrayList(prepareAccount()));
        doReturn(true).when(creditRepository).existsById(creditRequest.getId());
        doReturn(customer).when(customerService).getById(creditRequest.getCustomer());

        //test and verification
        try {
            creditService.saveCredit(creditRequest);
            fail("exception has not thrown");
        } catch (Exception e) {
            assertEquals(ErrorConstants.CUSTOMER_HAS_NOT_ANY_ACCOUNT_WITH_COMPATIBLE_CURRENCY_TO_CREDIT_CURRENCY, e.getMessage());
            assertEquals(BadRequestException.class, e.getClass());
            verify(creditRepository, times(1)).existsById(creditRequest.getId());
            verify(customerService, times(1)).getById(creditRequest.getCustomer());
        }
    }

    @Test
    public void testSaveCredit_exception() {
        //preparation
        CreditRequest creditRequest = prepareCreditRequest();
        Customer customer = prepareCustomer();
        doReturn(true).when(creditRepository).existsById(creditRequest.getId());
        doReturn(customer).when(customerService).getById(creditRequest.getCustomer());
        doThrow(new NullPointerException()).when(creditRepository).save(any());

        //test and verification
        try {
            creditService.saveCredit(creditRequest);
            fail("exception has not thrown");
        } catch (Exception e) {
            assertEquals(ErrorConstants.CREDIT_SAVE_FAILED, e.getMessage());
            assertEquals(BadRequestException.class, e.getClass());
            verify(creditRepository, times(1)).existsById(creditRequest.getId());
            verify(customerService, times(1)).getById(creditRequest.getCustomer());
            verify(creditRepository, times(1)).save(any());
        }
    }

    @Test
    public void testListCreditsByCustomer() {
        //preparation
        Credit credit = prepareCredit();
        credit.setCreatedDateTime(LocalDateTime.now().minusDays(credit.getTerm()));
        doReturn(Lists.newArrayList(credit))
                .when(creditRepository).findAllByCustomer_IdOrderByCreatedDateTimeDesc(credit.getCustomer().getId());

        //test
        List<CreditResponse> creditResponseList = creditService.listCreditsByCustomer(credit.getCustomer().getId(), true);

        //verification
        verify(creditRepository, times(1)).findAllByCustomer_IdOrderByCreatedDateTimeDesc(credit.getCustomer().getId());
        assertEquals(credit.getId(), creditResponseList.get(0).getId());
    }

    @Test
    public void testGetById() {
        //preparation
        Credit credit = prepareCredit();
        doReturn(Optional.of(credit)).when(creditRepository).findById(credit.getId());

        //test
        Credit responseCredit = creditService.getById(credit.getId());

        //verification
        verify(creditRepository, times(1)).findById(credit.getId());
        assertEquals(credit.getId(), responseCredit.getId());
    }

    @Test
    public void testGetById_exception() {
        //preparation
        Credit credit = prepareCredit();
        doReturn(Optional.empty()).when(creditRepository).findById(credit.getId());

        //test
        try{
            creditService.getById(credit.getId());
            fail("exception not thrown");
        } catch (Exception e) {
            assertEquals(BadRequestException.class, e.getClass());
            assertEquals(ErrorConstants.CREDIT_NOT_FOUND, e.getMessage());
        }

        //verification
        verify(creditRepository, times(1)).findById(credit.getId());
    }

    @Test
    public void testUpdateCreditsAndCustomerRatingsPerJob() {
        //test
        creditService.updateCreditsAndCustomerRatingsPerJob();

        //verification
        verify(creditRepository, times(1)).findAllByIsActive(true);
    }

    private Account prepareAccount() {
        return Account.builder()
                .id(2L)
                .currency(Currency.EUR).build();
    }

    private CreditRequest prepareCreditRequest() {
        return CreditRequest.builder()
                .id(1L)
                .term(10L)
                .creditAmount(BigDecimal.TEN)
                .currency(Currency.TRY)
                .customer(prepareCustomer().getId())
                .build();
    }

    private Credit prepareCredit() {
        return Credit.builder()
                .id(1L)
                .term(10L)
                .remainingTerm(5L)
                .creditAmount(BigDecimal.TEN)
                .remainingCreditAmount(BigDecimal.ONE)
                .currency(Currency.TRY)
                .customer(prepareCustomer())
                .isActive(true)
                .createdDateTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .build();
    }

    private Customer prepareCustomer() {
        return Customer.builder()
                .id(2L)
                .rating(2)
                .address("Istanbul")
                .name("yasin")
                .lastName("name")
                .dayOfBirth(LocalDate.of(1996, 3, 12))
                .build();
    }
}