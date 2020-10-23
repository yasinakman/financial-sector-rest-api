package com.akman.springbootdemo.rest_api.exceptions;

import com.akman.springbootdemo.request_response.ErrorMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        log.info("Exception type is BadRequestException");
        return new ResponseEntity<>(createErrorMessageRes(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnAuthorizedException.class)
    public ResponseEntity<Object> handleUnAuthorizedException(UnAuthorizedException ex) {
        log.info("Exception type is UnAuthorizedException");
        return new ResponseEntity<>(createErrorMessageRes(ex), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    private ErrorMessageResponse createErrorMessageRes(Exception ex) {
        logger.error("error: {}", ex);
        String errorCode = ex.getLocalizedMessage();
        if (errorCode == null) {errorCode = ex.toString();}
        return new ErrorMessageResponse(LocalDateTime.now(), errorCode);
    }
}
