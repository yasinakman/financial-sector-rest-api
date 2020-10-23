package com.akman.springbootdemo.request_response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageResponse {

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;
    private String errorCode;
}
