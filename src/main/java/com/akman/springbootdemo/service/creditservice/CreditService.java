package com.akman.springbootdemo.service.creditservice;

import com.akman.springbootdemo.model.account.AccountRequest;
import com.akman.springbootdemo.model.account.AccountResponse;
import com.akman.springbootdemo.model.credit.Credit;
import com.akman.springbootdemo.model.credit.CreditRequest;
import com.akman.springbootdemo.model.credit.CreditResponse;

import java.util.List;

public interface CreditService {

    CreditResponse saveCredit(CreditRequest creditRequest);

    List<CreditResponse> listCreditsByCustomer(Long customerId, boolean isExceeded);

    Credit getById(Long id);

    void updateCreditsAndCustomerRatingsPerJob();
}