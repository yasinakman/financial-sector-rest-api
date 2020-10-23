package com.akman.springbootdemo.request_response;

import com.akman.springbootdemo.model.account.Account;
import com.akman.springbootdemo.model.account.AccountResponse;
import com.akman.springbootdemo.model.credit.Credit;
import com.akman.springbootdemo.model.credit.CreditResponse;
import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.customer.CustomerResponse;
import com.akman.springbootdemo.model.transaction.Transaction;
import com.akman.springbootdemo.model.transaction.TransactionResponse;
import fr.xebia.extras.selma.*;

import java.util.List;

@Mapper
public interface ResponseMapper {

    ResponseMapper MAPPER = Selma.builder(ResponseMapper.class).build();

    @Maps(withIgnoreMissing = IgnoreMissing.ALL)
    CustomerResponse convertCustomerToCustomerResponse(Customer customer);
    @Maps(withIgnoreMissing = IgnoreMissing.ALL)
    List<CustomerResponse> convertCustomerListToCustomerResponseList(List<Customer> customerList);

    @Maps(withIgnoreMissing = IgnoreMissing.ALL)
    AccountResponse convertAccountToAccountResponse(Account account);
    @Maps(withIgnoreMissing = IgnoreMissing.ALL)
    List<AccountResponse> convertAccountListToAccountResponseList(List<Account> accountList);

    @Maps(withIgnoreMissing = IgnoreMissing.ALL)
    TransactionResponse convertTransactionToTransactionResponse(Transaction transaction);
    @Maps(withIgnoreMissing = IgnoreMissing.ALL)
    List<TransactionResponse> convertTransactionListToTransactionResponseList(List<Transaction> transactionList);

    @Maps(withIgnoreMissing = IgnoreMissing.ALL)
    CreditResponse convertCreditToCreditResponse(Credit CREDIT);
    @Maps(withIgnoreMissing = IgnoreMissing.ALL)
    List<CreditResponse> convertCreditListToCreditResponseList(List<Credit> CreditList);
}