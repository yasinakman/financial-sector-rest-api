package com.akman.springbootdemo.rest_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(String exception) {
        super(exception);
    }
}
