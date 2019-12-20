package com.biuwsi.common.exceptions;

public class BusinessException extends AbstractException {
    public BusinessException(Throwable cause, ExceptionErrorCode exceptionErrorCode) {
        super(cause, exceptionErrorCode);
    }

    public BusinessException(ExceptionErrorCode exceptionErrorCode) {
        super(exceptionErrorCode);
    }
}
