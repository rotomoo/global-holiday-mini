package com.globalholidaymini.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ;

    private final int customCode;
    private final HttpStatus httpStatus;
    private final String msg;
}
