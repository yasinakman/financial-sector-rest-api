package com.akman.springbootdemo.service.accountservice;

import com.akman.springbootdemo.model.account.Account;
import com.akman.springbootdemo.model.account.AccountRequest;
import com.akman.springbootdemo.model.account.AccountResponse;

import java.util.List;

public interface AccountService {

    AccountResponse saveAccount(AccountRequest accountRequest);

    List<AccountResponse> listAccountsByCustomer(Long customerId);

    Account getById(Long id);
}