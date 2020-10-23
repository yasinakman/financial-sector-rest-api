package com.akman.springbootdemo.request_response;

import com.akman.springbootdemo.model.account.Account;
import com.akman.springbootdemo.model.account.AccountRequest;
import com.akman.springbootdemo.model.credit.Credit;
import com.akman.springbootdemo.model.credit.CreditRequest;
import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.customer.CustomerRequest;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.model.transaction.Transaction;
import com.akman.springbootdemo.model.transaction.TransactionRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

class RequestMapperTest {

    @Test
    void testConvertFootballClubRequestToFootballClub() {
        CustomerRequest customerRequest = prepareCustomerRequest();

        Customer customer = RequestMapper.MAPPER.convertCustomerRequestToCustomer(customerRequest);
        if (customer == null) {
            fail("customer is null");
        }
        assertEquals(customerRequest.getId(), customer.getId());
        assertEquals(customerRequest.getName(), customer.getName());
        assertEquals(customerRequest.getLastName(), customer.getLastName());
        assertEquals(customerRequest.getDayOfBirth(), customer.getDayOfBirth());
        assertEquals(customerRequest.getAddress(), customer.getAddress());
        assertEquals(customerRequest.getRating(), customer.getRating());
    }

    @Test
    void testConvertAccountRequestToAccount() {
        AccountRequest accountRequest = prepareAccountRequest();

        Account account = RequestMapper.MAPPER.convertAccountRequestToAccount(accountRequest);
        if (account == null) {
            fail("account is null");
        }
        assertEquals(accountRequest.getId(), account.getId());
        assertEquals(accountRequest.getCustomer(), account.getCustomer().getId());
        assertEquals(accountRequest.getCurrency(), account.getCurrency());
        assertEquals(accountRequest.getBalance(), account.getBalance());
        assertEquals(accountRequest.getNo(), account.getNo());
    }

    @Test
    void testConvertTransactionRequestToTransaction() {
        TransactionRequest transactionRequest = prepareTransactionRequest();

        Transaction transaction = RequestMapper.MAPPER.convertTransactionRequestToTransaction(transactionRequest);
        if (transaction == null) {
            fail("transaction is null");
        }
        assertEquals(transactionRequest.isCreditPayment(), transaction.isCreditPayment());
        assertEquals(transactionRequest.getCurrency(), transaction.getCurrency());
        assertEquals(transactionRequest.getFromAccount(), transaction.getFromAccount().getId());
        assertEquals(transactionRequest.getToAccount(), transaction.getToAccount().getId());
        assertEquals(transactionRequest.getTotal(), transaction.getTotal());
        assertEquals(transactionRequest.getToCredit(), transaction.getToCredit().getId());
    }

    @Test
    void testConvertCreditRequestToCredit() {
        CreditRequest creditRequest = prepareCreditRequest();
        Credit credit = RequestMapper.MAPPER.convertCreditRequestToCredit(creditRequest);
        if (credit == null) {
            fail("credit is null");
        }
        assertEquals(creditRequest.getId(), credit.getId());
        assertEquals(creditRequest.getTerm(), credit.getTerm());
        assertEquals(creditRequest.getCreditAmount(), credit.getCreditAmount());
        assertEquals(creditRequest.getCurrency(), credit.getCurrency());
        assertEquals(creditRequest.getCustomer(), credit.getCustomer().getId());
    }

    private CreditRequest prepareCreditRequest() {
        return CreditRequest.builder()
                .id(1L)
                .term(10L)
                .creditAmount(BigDecimal.TEN)
                .currency(Currency.TRY)
                .customer(2L)
                .build();
    }

    private TransactionRequest prepareTransactionRequest() {
        return TransactionRequest.builder()
                .isCreditPayment(false)
                .currency(Currency.TRY)
                .fromAccount(1L)
                .toAccount(2L)
                .total(BigDecimal.TEN)
                .toCredit(3L)
                .build();
    }

    private AccountRequest prepareAccountRequest() {
        return AccountRequest.builder()
                .id(1L)
                .customer(1L)
                .currency(Currency.TRY)
                .no(123456L)
                .balance(BigDecimal.valueOf(50000))
                .build();
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
}