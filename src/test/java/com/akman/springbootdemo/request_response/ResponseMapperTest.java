package com.akman.springbootdemo.request_response;

import com.akman.springbootdemo.model.account.Account;
import com.akman.springbootdemo.model.account.AccountResponse;
import com.akman.springbootdemo.model.credit.Credit;
import com.akman.springbootdemo.model.credit.CreditResponse;
import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.model.transaction.Transaction;
import com.akman.springbootdemo.model.transaction.TransactionResponse;
import com.google.common.collect.Lists;
import io.jsonwebtoken.lang.Collections;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

class ResponseMapperTest {

    @Test
    void testConvertCustomerListToCustomerResponseList() {
        Customer customer = prepareCustomer();
        List<CustomerResponse> customerResponseList =
                ResponseMapper.MAPPER.convertCustomerListToCustomerResponseList(Lists.newArrayList(customer));
        if (Collections.isEmpty(customerResponseList)) {
            fail("customerResponseList is null or empty");
        }
        CustomerResponse customerResponse = customerResponseList.get(0);

        assertEquals(customer.getId(), customerResponse.getId());
        assertEquals(customer.getName(), customerResponse.getName());
        assertEquals(customer.getLastName(), customerResponse.getLastName());
        assertEquals(customer.getDayOfBirth(), customerResponse.getDayOfBirth());
        assertEquals(customer.getAddress(), customerResponse.getAddress());
        assertEquals(customer.getRating(), customerResponse.getRating());
    }

    @Test
    void testConvertAccountListToAccountResponseList() {
        Account account = prepareAccount();
        List<AccountResponse> accountResponseList =
                ResponseMapper.MAPPER.convertAccountListToAccountResponseList(Lists.newArrayList(account));
        if (Collections.isEmpty(accountResponseList)) {
            fail("accountResponseList is null or empty");
        }
        AccountResponse accountResponse = accountResponseList.get(0);

        assertEquals(account.getId(), accountResponse.getId());
        assertEquals(account.getBalance(), accountResponse.getBalance());
        assertEquals(account.getCustomer().getId(), accountResponse.getCustomer().getId());
        assertEquals(account.getCurrency(), accountResponse.getCurrency());
        assertEquals(account.getNo(), accountResponse.getNo());
        assertEquals(account.getCreatedDateTime(), accountResponse.getCreatedDateTime());
    }

    @Test
    void testConvertTransactionListToTransactionResponseList() {
        Transaction transaction = prepareTransaction();
        List<TransactionResponse> transactionResponseList =
                ResponseMapper.MAPPER.convertTransactionListToTransactionResponseList(Lists.newArrayList(transaction));
        if (Collections.isEmpty(transactionResponseList)) {
            fail("transactionResponseList is null or empty");
        }
        TransactionResponse transactionResponse = transactionResponseList.get(0);

        assertEquals(transaction.getId(), transactionResponse.getId());
        assertEquals(transaction.getTotal(), transactionResponse.getTotal());
        assertEquals(transaction.getCurrency(), transactionResponse.getCurrency());
        assertEquals(transaction.getFromAccount().getId(), transactionResponse.getFromAccount().getId());
        assertEquals(transaction.getToAccount().getId(), transactionResponse.getToAccount().getId());
        assertEquals(transaction.getTransactionTime(), transactionResponse.getTransactionTime());
        assertEquals(transaction.isCreditPayment(), transactionResponse.isCreditPayment());
    }

    @Test
    void testConvertCreditListToCreditResponseList() {
        Credit credit = prepareCredit();
        List<CreditResponse> creditResponsesList = ResponseMapper.MAPPER.convertCreditListToCreditResponseList(Lists.newArrayList(credit));
        if (Collections.isEmpty(creditResponsesList)) {
            fail("creditResponsesList is null or empty");
        }
        CreditResponse creditResponse = creditResponsesList.get(0);

        assertEquals(credit.getId(), creditResponse.getId());
        assertEquals(credit.getTerm(), creditResponse.getTerm());
        assertEquals(credit.getRemainingCreditAmount(), creditResponse.getRemainingCreditAmount());
        assertEquals(credit.getCreditAmount(), creditResponse.getCreditAmount());
        assertEquals(credit.getRemainingCreditAmount(), creditResponse.getRemainingCreditAmount());
        assertEquals(credit.getCurrency(), creditResponse.getCurrency());
        assertEquals(credit.getCustomer().getId(), creditResponse.getCustomer().getId());
        assertEquals(credit.isActive(), creditResponse.isActive());
        assertEquals(credit.getCreatedDateTime(), creditResponse.getCreatedDateTime());
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
                .incomingTransactionList(null)
                .isActive(true)
                .createdDateTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .build();
    }

    private Transaction prepareTransaction() {
        return Transaction.builder()
                .id(1L)
                .total(BigDecimal.ONE)
                .currency(Currency.TRY)
                .fromAccount(prepareAccount2())
                .toAccount(prepareAccount())
                .transactionTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .isCreditPayment(true)
                .build();
    }

    private Account prepareAccount() {
        return Account.builder()
                .id(1L)
                .balance(BigDecimal.TEN)
                .customer(prepareCustomer())
                .currency(Currency.TRY)
                .no(123456L)
                .createdDateTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .incomingTransactionList(null)
                .outgoingTransactionList(null)
                .build();
    }

    private Account prepareAccount2() {
        return Account.builder()
                .id(2L)
                .balance(BigDecimal.TEN)
                .customer(prepareCustomer())
                .currency(Currency.TRY)
                .no(654321L)
                .createdDateTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .incomingTransactionList(null)
                .outgoingTransactionList(null)
                .build();
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
