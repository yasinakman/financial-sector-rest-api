package com.akman.springbootdemo.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Currency {

    TRY("TRY"),
    USD("USD"),
    EUR("EUR");

    @Getter
    private String value;
}