package com.biuwsi.common.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class AbstractException extends RuntimeException implements CommonException {
    private String errorId;
    private String errorCode;

    public AbstractException(Throwable cause, ExceptionErrorCode exceptionErrorCode) {
        super(cause);
        init(exceptionErrorCode);
    }

    public AbstractException(ExceptionErrorCode exceptionErrorCode) {
        init(exceptionErrorCode);
    }

    private void init(ExceptionErrorCode exceptionErrorCode) {
        errorId = UUID.randomUUID().toString();
        errorCode = exceptionErrorCode.getCode();
    }
}
