package com.akman.springbootdemo.service.transactionservice;

import com.akman.springbootdemo.model.account.Account;
import com.akman.springbootdemo.model.credit.Credit;
import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.enums.AscOrDesc;
import com.akman.springbootdemo.model.enums.TransactionField;
import com.akman.springbootdemo.model.transaction.Transaction;
import com.akman.springbootdemo.model.transaction.TransactionRequest;
import com.akman.springbootdemo.model.transaction.TransactionResponse;
import com.akman.springbootdemo.repository.TransactionRepository;
import com.akman.springbootdemo.request_response.RequestMapper;
import com.akman.springbootdemo.request_response.ResponseMapper;
import com.akman.springbootdemo.rest_api.exceptions.BadRequestException;
import com.akman.springbootdemo.service.accountservice.AccountService;
import com.akman.springbootdemo.service.creditservice.CreditService;
import com.akman.springbootdemo.utils.ErrorConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final CreditService creditService;

    @Transactional
    @Override
    public TransactionResponse saveTransaction(TransactionRequest transactionRequest) {
        Long toAccount = transactionRequest.getToAccount();
        if (transactionRequest.getFromAccount().equals(toAccount)) {
            throw new BadRequestException(ErrorConstants.INCOMING_AND_OUTGOING_ACCOUNT_IDS_ARE_EQUAL);
        }
        Transaction transaction = RequestMapper.MAPPER.convertTransactionRequestToTransaction(transactionRequest);
        updateAccountsAndTransaction(transaction);
        Transaction save = transactionRepository.save(transaction);
        return ResponseMapper.MAPPER.convertTransactionToTransactionResponse(save);
    }

    private void updateAccountsAndTransaction(Transaction transaction) {
        Account fromAccount = accountService.getById(transaction.getFromAccount().getId());
        fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getTotal()));
        transaction.setFromAccount(fromAccount);
        if (transaction.isCreditPayment()) {
            if (transaction.getToCredit() == null) {
                throw new BadRequestException(ErrorConstants.INCOMING_CREDIT_ID_WAS_NOT_GIVEN);
            }
            Credit toCredit = creditService.getById(transaction.getToCredit().getId());
            if (!toCredit.isActive()) {
                throw new BadRequestException(ErrorConstants.CREDIT_IS_NOT_ACTIVE);
            }
            BigDecimal updatedRemainingAmount = toCredit.getRemainingCreditAmount().subtract(transaction.getTotal());
            toCredit.setRemainingCreditAmount(updatedRemainingAmount);
            timeNotExceeded(toCredit);
            transaction.setToCredit(toCredit);
        } else {
            if (transaction.getToAccount() == null) {
                throw new BadRequestException(ErrorConstants.INCOMING_ACCOUNT_ID_WAS_NOT_GIVEN);
            }
            Account toAccount = accountService.getById(transaction.getToAccount().getId());
            toAccount.setBalance(toAccount.getBalance().add(transaction.getTotal()));
            transaction.setToAccount(toAccount);
        }
    }

    private void timeNotExceeded(Credit creditToUpdate) {
        if (BigDecimal.ZERO.compareTo(creditToUpdate.getRemainingCreditAmount()) >= 0) {
            Customer customer = creditToUpdate.getCustomer();
            int rating = customer.getRating();
            customer.setRating(rating > 1 ? rating - 1 : 1);
            creditToUpdate.setActive(false);
        }
    }

    @Override
    public List<TransactionResponse> listTransactionsByAccount(Long accountId) {
        List<Transaction> transactionsByAccount = transactionRepository
                .findAllByFromAccount_IdOrToAccount_IdOrderByTransactionTimeDesc(accountId, accountId);
        return ResponseMapper.MAPPER.convertTransactionListToTransactionResponseList(transactionsByAccount);
    }

    @Override
    public List<TransactionResponse> listIncomingTransactionsByAccount(Long accountId) {
        List<Transaction> transactionsByAccount = transactionRepository.findAllByToAccount_IdOrderByTransactionTimeDesc(accountId);
        return ResponseMapper.MAPPER.convertTransactionListToTransactionResponseList(transactionsByAccount);
    }

    @Override
    public List<TransactionResponse> listOutgoingTransactionsByAccount(Long accountId) {
        List<Transaction> transactionsByAccount = transactionRepository.findAllByFromAccount_IdOrderByTransactionTimeDesc(accountId);
        return ResponseMapper.MAPPER.convertTransactionListToTransactionResponseList(transactionsByAccount);
    }

    @Override
    public List<TransactionResponse> listTransactionsByCredit(Long creditId) {
        List<Transaction> transactionsByAccount = transactionRepository.findAllByToCredit_IdOrderByTransactionTimeDesc(creditId);
        return ResponseMapper.MAPPER.convertTransactionListToTransactionResponseList(transactionsByAccount);
    }

    @Override
    public Page<TransactionResponse> listAllTransactionsPageable(String name, Long accountId, TransactionField transactionField,
                                                                 AscOrDesc ascOrDesc, LocalDateTime startDateTime,
                                                                 LocalDateTime endDateTime, Integer pageNo, Integer pageSize) {
        Sort sort = getOrders(transactionField, ascOrDesc);
        return transactionRepository.findAllByFromAccount_Customer_NameAndFromAccount_IdOrToAccount_Id(name, accountId,
                PageRequest.of(pageNo, pageSize, sort)).map(TransactionResponse::fromTransaction);
    }

    @Override
    public List<TransactionResponse> listAllTransactions(String name, Long accountId, TransactionField transactionField,
                                                         AscOrDesc ascOrDesc, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Sort sort = getOrders(transactionField, ascOrDesc);
        List<Transaction> transactionsByAccount = transactionRepository
                .findAllByFromAccount_Customer_NameAndFromAccount_IdOrToAccount_Id(name, accountId, sort);
        return ResponseMapper.MAPPER.convertTransactionListToTransactionResponseList(transactionsByAccount);
    }

    private Sort getOrders(TransactionField transactionField, AscOrDesc ascOrDesc) {
        return AscOrDesc.ASCENDING.equals(ascOrDesc)
                ? Sort.by(transactionField.getValue()).ascending() : Sort.by(transactionField.getValue()).descending();
    }
}
