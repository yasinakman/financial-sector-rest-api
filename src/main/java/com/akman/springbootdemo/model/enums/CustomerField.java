package com.akman.springbootdemo.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CustomerField {
    NAME("name"),
    LAST_NAME("lastName"),
    ADDRESS("address"),
    DAY_OF_BIRTH("dayOfBirth"),
    RATING("rating");

    @Getter
    private String value;
}