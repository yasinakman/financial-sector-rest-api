package com.akman.springbootdemo.request_response;

import com.akman.springbootdemo.model.account.Account;
import com.akman.springbootdemo.model.account.AccountRequest;
import com.akman.springbootdemo.model.credit.Credit;
import com.akman.springbootdemo.model.credit.CreditRequest;
import com.akman.springbootdemo.model.customer.Customer;
import com.akman.springbootdemo.model.customer.CustomerRequest;
import com.akman.springbootdemo.model.transaction.Transaction;
import com.akman.springbootdemo.model.transaction.TransactionRequest;
import fr.xebia.extras.selma.*;

@Mapper
public interface RequestMapper {

    RequestMapper MAPPER = Selma.builder(RequestMapper.class).build();

    @Maps(withIgnoreMissing = IgnoreMissing.ALL)
    Customer convertCustomerRequestToCustomer(CustomerRequest customerRequest);

    @Maps(withIgnoreMissing = IgnoreMissing.ALL,
            withCustomFields = {
                    @Field({"customer", "customer.id"})
            })
    Account convertAccountRequestToAccount(AccountRequest accountRequest);

    @Maps(withIgnoreMissing = IgnoreMissing.ALL,
            withCustomFields = {
                    @Field({"fromAccount", "fromAccount.id"}),
                    @Field({"toAccount", "toAccount.id"}),
                    @Field({"toCredit", "toCredit.id"})
            })
    Transaction convertTransactionRequestToTransaction(TransactionRequest transactionRequest);

    @Maps(withIgnoreMissing = IgnoreMissing.ALL,
            withCustomFields = {
                    @Field({"customer", "customer.id"})
            })
    Credit convertCreditRequestToCredit(CreditRequest creditRequest);
}
