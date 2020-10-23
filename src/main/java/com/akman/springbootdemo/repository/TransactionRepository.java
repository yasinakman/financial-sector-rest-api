package com.akman.springbootdemo.repository;

import com.akman.springbootdemo.model.transaction.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByFromAccount_IdOrToAccount_IdOrderByTransactionTimeDesc(Long fromAccountId, Long toAccountId);

    List<Transaction> findAllByFromAccount_IdOrderByTransactionTimeDesc(Long fromAccountId);

    List<Transaction> findAllByToAccount_IdOrderByTransactionTimeDesc(Long toAccountId);

    List<Transaction> findAllByToCredit_IdOrderByTransactionTimeDesc(Long toAccountId);

    @Query("select t from Transaction t where :name is null or t.fromAccount.customer.name = :name and" +
            ":accountId is null or t.fromAccount.id = :accountId or t.toAccount.id = :accountId")
    Page<Transaction> findAllByFromAccount_Customer_NameAndFromAccount_IdOrToAccount_Id(@Param("name") String name, @Param("accountId") Long accountId, Pageable pageable);

    @Query("select t from Transaction t where :name is null or t.fromAccount.customer.name = :name and" +
            ":accountId is null or t.fromAccount.id = :accountId or t.toAccount.id = :accountId")
    List<Transaction> findAllByFromAccount_Customer_NameAndFromAccount_IdOrToAccount_Id(@Param("name") String name, @Param("accountId") Long accountId, Sort sort);
}
