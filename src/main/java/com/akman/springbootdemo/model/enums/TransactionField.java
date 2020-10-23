package com.akman.springbootdemo.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TransactionField {
    CREDIT_CREATED_TIME("toCredit.createdDateTime"),
    TOTAL("total"),
    IS_CREDIT_PAYMENT("isCreditPayment"),
    TRANSACTION_TIME("transactionTime");

    @Getter
    private String value;
}