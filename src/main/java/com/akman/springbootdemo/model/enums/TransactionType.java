package com.akman.springbootdemo.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TransactionType {
    IN_OUT, INCOMING, OUTGOING
}