package com.akman.springbootdemo.service;

import com.akman.springbootdemo.model.account.Account;
import com.akman.springbootdemo.model.account.AccountResponse;
import com.akman.springbootdemo.model.credit.Credit;
import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.enums.AscOrDesc;
import com.akman.springbootdemo.model.enums.Currency;
import com.akman.springbootdemo.model.enums.TransactionField;
import com.akman.springbootdemo.model.transaction.Transaction;
import com.akman.springbootdemo.model.transaction.TransactionRequest;
import com.akman.springbootdemo.model.transaction.TransactionResponse;
import com.akman.springbootdemo.repository.TransactionRepository;
import com.akman.springbootdemo.rest_api.exceptions.BadRequestException;
import com.akman.springbootdemo.service.accountservice.AccountService;
import com.akman.springbootdemo.service.creditservice.CreditService;
import com.akman.springbootdemo.service.transactionservice.TransactionServiceImpl;
import com.akman.springbootdemo.utils.ErrorConstants;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TransactionServiceImpl.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private CreditService creditService;

    @Test
    public void testSaveTransaction() {
        //preparation
        TransactionRequest transactionRequest = prepareTransactionRequest();
        Transaction transactionToSave = prepareTransaction();
        transactionToSave.setId(null);
        Transaction transaction = prepareTransaction();
        doReturn(prepareAccount()).when(accountService).getById(transactionToSave.getFromAccount().getId());
        doReturn(prepareAccount2()).when(accountService).getById(transactionToSave.getToAccount().getId());
        doReturn(transaction).when(transactionRepository).save(transactionToSave);

        //test
        transactionService.saveTransaction(transactionRequest);

        //verification
        verify(accountService, times(1)).getById(transactionToSave.getFromAccount().getId());
        verify(accountService, times(1)).getById(transactionToSave.getFromAccount().getId());
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    public void testSaveTransaction_credit() {
        //preparation
        TransactionRequest transactionRequest = prepareTransactionCreditRequest();
        Transaction transactionToSave = prepareTransactionCredit();
        transactionToSave.setId(null);
        Transaction transaction = prepareTransaction();
        doReturn(prepareAccount()).when(accountService).getById(transactionToSave.getFromAccount().getId());
        doReturn(prepareCredit()).when(creditService).getById(transactionToSave.getToCredit().getId());
        doReturn(transaction).when(transactionRepository).save(transactionToSave);

        //test
        transactionService.saveTransaction(transactionRequest);

        //verification
        verify(accountService, times(1)).getById(transactionToSave.getFromAccount().getId());
        verify(creditService, times(1)).getById(transactionToSave.getToCredit().getId());
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    public void testSaveTransaction_fromAccountEqualToAccount_exception() {
        //preparation
        TransactionRequest transactionRequest = prepareTransactionRequest();
        transactionRequest.setToAccount(transactionRequest.getFromAccount());

        //test and verification
        try {
            transactionService.saveTransaction(transactionRequest);
            fail("exception not thrown");
        } catch (Exception e) {
            assertEquals(BadRequestException.class, e.getClass());
            assertEquals(ErrorConstants.INCOMING_AND_OUTGOING_ACCOUNT_IDS_ARE_EQUAL, e.getMessage());
        }
    }

    @Test
    public void testSaveTransaction_incomingAccountIdNull_exception() {
        //preparation
        TransactionRequest transactionRequest = prepareTransactionRequest();
        transactionRequest.setToAccount(null);
        Transaction transactionToSave = prepareTransaction();
        transactionToSave.setId(null);
        transactionToSave.setToAccount(null);
        doReturn(prepareAccount()).when(accountService).getById(transactionToSave.getFromAccount().getId());

        //test and verification
        try {
            transactionService.saveTransaction(transactionRequest);
            fail("exception not thrown");
        } catch (Exception e) {
            assertEquals(BadRequestException.class, e.getClass());
            assertEquals(ErrorConstants.INCOMING_ACCOUNT_ID_WAS_NOT_GIVEN, e.getMessage());
            verify(accountService, times(1)).getById(transactionToSave.getFromAccount().getId());
        }
    }

    @Test
    public void testSaveTransaction_incomingCreditIdNull_exception() {
        //preparation
        TransactionRequest transactionRequest = prepareTransactionCreditRequest();
        transactionRequest.setToCredit(null);
        Transaction transactionToSave = prepareTransactionCredit();
        transactionToSave.setId(null);
        transactionToSave.setToCredit(null);
        doReturn(prepareAccount()).when(accountService).getById(transactionToSave.getFromAccount().getId());

        //test and verification
        try {
            transactionService.saveTransaction(transactionRequest);
            fail("exception not thrown");
        } catch (Exception e) {
            assertEquals(BadRequestException.class, e.getClass());
            assertEquals(ErrorConstants.INCOMING_CREDIT_ID_WAS_NOT_GIVEN, e.getMessage());
            verify(accountService, times(1)).getById(transactionToSave.getFromAccount().getId());
        }
    }

    @Test
    public void testSaveTransaction_incomingCreditIsNotActive_exception() {
        //preparation
        TransactionRequest transactionRequest = prepareTransactionCreditRequest();
        Transaction transactionToSave = prepareTransactionCredit();
        transactionToSave.setId(null);
        doReturn(prepareAccount()).when(accountService).getById(transactionToSave.getFromAccount().getId());
        Credit credit = prepareCredit();
        credit.setActive(false);
        doReturn(credit).when(creditService).getById(transactionToSave.getToCredit().getId());

        //test and verification
        try {
            transactionService.saveTransaction(transactionRequest);
            fail("exception not thrown");
        } catch (Exception e) {
            assertEquals(BadRequestException.class, e.getClass());
            assertEquals(ErrorConstants.CREDIT_IS_NOT_ACTIVE, e.getMessage());
            verify(accountService, times(1)).getById(transactionToSave.getFromAccount().getId());
            verify(creditService, times(1)).getById(transactionToSave.getToCredit().getId());
        }
    }

    @Test
    public void testListTransactionsByAccount() {
        //preparation
        Transaction transaction = prepareTransaction();
        doReturn(Lists.newArrayList(transaction)).when(transactionRepository)
                .findAllByFromAccount_IdOrToAccount_IdOrderByTransactionTimeDesc(transaction.getId(), transaction.getId());

        //test
        List<TransactionResponse> transactionResponseList = transactionService.listTransactionsByAccount(transaction.getId());

        //verification
        verify(transactionRepository, times(1))
                .findAllByFromAccount_IdOrToAccount_IdOrderByTransactionTimeDesc(transaction.getId(), transaction.getId());
        assertEquals(transaction.getFromAccount().getId(), transactionResponseList.get(0).getFromAccount().getId());
        assertEquals(transaction.getToAccount().getId(), transactionResponseList.get(0).getToAccount().getId());
        assertEquals(transaction.getTotal(), transactionResponseList.get(0).getTotal());
    }

    @Test
    public void testListIncomingTransactionsByAccount() {
        //preparation
        Transaction transaction = prepareTransaction();
        doReturn(Lists.newArrayList(transaction)).when(transactionRepository)
                .findAllByToAccount_IdOrderByTransactionTimeDesc(transaction.getId());

        //test
        List<TransactionResponse> transactionResponseList =
                transactionService.listIncomingTransactionsByAccount(transaction.getId());

        //verification
        verify(transactionRepository, times(1))
                .findAllByToAccount_IdOrderByTransactionTimeDesc(transaction.getId());
        assertEquals(transaction.getFromAccount().getId(), transactionResponseList.get(0).getFromAccount().getId());
        assertEquals(transaction.getToAccount().getId(), transactionResponseList.get(0).getToAccount().getId());
        assertEquals(transaction.getTotal(), transactionResponseList.get(0).getTotal());
    }

    @Test
    public void testListOutgoingTransactionsByAccount() {
        //preparation
        Transaction transaction = prepareTransaction();
        doReturn(Lists.newArrayList(transaction)).when(transactionRepository)
                .findAllByFromAccount_IdOrderByTransactionTimeDesc(transaction.getId());

        //test
        List<TransactionResponse> transactionResponseList =
                transactionService.listOutgoingTransactionsByAccount(transaction.getId());

        //verification
        verify(transactionRepository, times(1))
                .findAllByFromAccount_IdOrderByTransactionTimeDesc(transaction.getId());
        assertEquals(transaction.getFromAccount().getId(), transactionResponseList.get(0).getFromAccount().getId());
        assertEquals(transaction.getToAccount().getId(), transactionResponseList.get(0).getToAccount().getId());
        assertEquals(transaction.getTotal(), transactionResponseList.get(0).getTotal());
    }

    @Test
    public void testListTransactionsByCredit() {
        //preparation
        Transaction transaction = prepareTransactionCredit();
        Credit credit = prepareCredit();
        doReturn(Lists.newArrayList(transaction)).when(transactionRepository)
                .findAllByToCredit_IdOrderByTransactionTimeDesc(credit.getId());

        //test
        List<TransactionResponse> transactionResponseList = transactionService.listTransactionsByCredit(credit.getId());

        //verification
        verify(transactionRepository, times(1))
                .findAllByToCredit_IdOrderByTransactionTimeDesc(credit.getId());
        assertEquals(transaction.getFromAccount().getId(), transactionResponseList.get(0).getFromAccount().getId());
        assertEquals(transaction.getToCredit().getId(), transactionResponseList.get(0).getToCredit().getId());
        assertEquals(transaction.getTotal(), transactionResponseList.get(0).getTotal());
    }

    @Test
    public void testListAllTransactionsPageable() {
        //preparation
        final int pageNo = 0;
        final int pageSize = 10;
        Transaction transaction = prepareTransaction();
        Customer customer = prepareCustomer();
        Account account = prepareAccount();
        String transactionField = TransactionField.TRANSACTION_TIME.getValue();
        Sort sort = Sort.by(transactionField).descending();
        PageImpl<Transaction> pageableTransactions = getPageable(Lists.newArrayList(transaction));
        doReturn(pageableTransactions).when(transactionRepository)
                .findAllByFromAccount_Customer_NameAndFromAccount_IdOrToAccount_Id(customer.getName(), account.getId(),
                        PageRequest.of(pageNo, pageSize, sort));

        //test
        Page<TransactionResponse> transactionResponsePage = transactionService
                .listAllTransactionsPageable(customer.getName(), account.getId(), TransactionField.TRANSACTION_TIME,
                        AscOrDesc.DESCENDING, transaction.getTransactionTime().plusHours(1L),
                        transaction.getTransactionTime().minusHours(1L), pageNo, pageSize);

        //verification
        verify(transactionRepository, times(1))
                .findAllByFromAccount_Customer_NameAndFromAccount_IdOrToAccount_Id(customer.getName(), account.getId(),
                        PageRequest.of(pageNo, pageSize, sort));
        assertEquals(transaction.getFromAccount().getId(), transactionResponsePage.getContent().get(0).getFromAccount().getId());
        assertEquals(transaction.getToAccount().getId(), transactionResponsePage.getContent().get(0).getToAccount().getId());
        assertEquals(transaction.getTotal(), transactionResponsePage.getContent().get(0).getTotal());
    }
    @Test
    public void testListAllTransactions() {
        //preparation
        Transaction transaction = prepareTransaction();
        Customer customer = prepareCustomer();
        Account account = prepareAccount();
        String transactionField = TransactionField.TRANSACTION_TIME.getValue();
        Sort sort = Sort.by(transactionField).ascending();
        ArrayList<Transaction> transactionList = Lists.newArrayList(transaction);
        doReturn(transactionList).when(transactionRepository).findAllByFromAccount_Customer_NameAndFromAccount_IdOrToAccount_Id(
                        customer.getName(), account.getId(), sort);

        //test
        List<TransactionResponse> transactionResponseList = transactionService
                .listAllTransactions(customer.getName(), account.getId(), TransactionField.TRANSACTION_TIME,
                        AscOrDesc.ASCENDING, transaction.getTransactionTime().plusHours(1L),
                        transaction.getTransactionTime().minusHours(1L));

        //verification
        verify(transactionRepository, times(1))
                .findAllByFromAccount_Customer_NameAndFromAccount_IdOrToAccount_Id(
                        customer.getName(), account.getId(), sort);
        assertEquals(transaction.getFromAccount().getId(), transactionResponseList.get(0).getFromAccount().getId());
        assertEquals(transaction.getToAccount().getId(), transactionResponseList.get(0).getToAccount().getId());
        assertEquals(transaction.getTotal(), transactionResponseList.get(0).getTotal());
    }

    private <T> PageImpl<T> getPageable(List<T> list) {
        return new PageImpl<>(list);
    }

    private Transaction prepareTransaction() {
        return Transaction.builder()
                .isCreditPayment(false)
                .transactionTime(LocalDate.of(2020, 3, 12).atStartOfDay())
                .fromAccount(prepareAccount())
                .toAccount(prepareAccount2())
                .currency(Currency.TRY)
                .total(BigDecimal.ONE)
                .id(1L)
                .build();
    }

    private TransactionRequest prepareTransactionRequest() {
        return TransactionRequest.builder()
                .isCreditPayment(false)
                .currency(Currency.TRY)
                .total(BigDecimal.ONE)
                .fromAccount(prepareAccount().getId())
                .toAccount(prepareAccount2().getId())
                .build();
    }

    private Transaction prepareTransactionCredit() {
        return Transaction.builder()
                .isCreditPayment(false)
                .transactionTime(LocalDate.of(2020, 3, 12).atStartOfDay())
                .fromAccount(prepareAccount())
                .toCredit(prepareCredit())
                .currency(Currency.TRY)
                .total(BigDecimal.ONE)
                .id(1L)
                .build();
    }

    private TransactionRequest prepareTransactionCreditRequest() {
        return TransactionRequest.builder()
                .isCreditPayment(true)
                .currency(Currency.TRY)
                .total(BigDecimal.ONE)
                .fromAccount(prepareAccount().getId())
                .toCredit(prepareCredit().getId())
                .build();
    }

    private Credit prepareCredit() {
        return Credit.builder()
                .id(1L)
                .remainingTerm(5L)
                .term(10L)
                .remainingCreditAmount(BigDecimal.ONE)
                .createdDateTime(LocalDate.of(2020, 3, 12).atStartOfDay())
                .isActive(true)
                .customer(prepareCustomer())
                .currency(Currency.TRY)
                .creditAmount(BigDecimal.TEN)
                .build();
    }

    private Account prepareAccount() {
        return Account.builder()
                .id(2L)
                .balance(BigDecimal.TEN)
                .currency(Currency.TRY)
                .customer(prepareCustomer())
                .no(123546L)
                .createdDateTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .build();
    }

    private Account prepareAccount2() {
        return Account.builder()
                .id(5L)
                .balance(BigDecimal.TEN)
                .currency(Currency.TRY)
                .customer(prepareCustomer())
                .no(654321L)
                .createdDateTime(LocalDate.of(2020, 12, 12).atStartOfDay())
                .build();
    }

    private Customer prepareCustomer() {
        return Customer.builder().id(2L).build();
    }
}
