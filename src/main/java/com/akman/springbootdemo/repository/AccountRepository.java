package com.akman.springbootdemo.repository;

import com.akman.springbootdemo.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByCustomer_IdOrderByCreatedDateTimeDesc(Long customerId);
}