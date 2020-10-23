package com.akman.springbootdemo.service.transactionservice;

import com.akman.springbootdemo.model.enums.AscOrDesc;
import com.akman.springbootdemo.model.enums.TransactionField;
import com.akman.springbootdemo.model.transaction.TransactionRequest;
import com.akman.springbootdemo.model.transaction.TransactionResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    TransactionResponse saveTransaction(TransactionRequest transactionRequest);

    List<TransactionResponse> listTransactionsByAccount(Long accountId);

    List<TransactionResponse> listIncomingTransactionsByAccount(Long accountId);

    List<TransactionResponse> listOutgoingTransactionsByAccount(Long accountId);

    List<TransactionResponse> listTransactionsByCredit(Long creditId);

    Page<TransactionResponse> listAllTransactionsPageable(String name, Long accountId, TransactionField transactionField, AscOrDesc ascOrDesc,
                                                          LocalDateTime startDateTime, LocalDateTime endDateTime, Integer pageNo, Integer pageSize);

    List<TransactionResponse> listAllTransactions(String name, Long accountId, TransactionField transactionField,
                                                  AscOrDesc ascOrDesc, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
