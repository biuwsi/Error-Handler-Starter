package com.biuwsi.starter.error.handler;

import com.biuwsi.common.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Slf4j
public class BaseErrorHandler {
    public static final String DEFAULT_ERROR_CODE = "internal.error";

    @Autowired
    private ErrorResponseMapper errorResponseMapper;

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity handleThrowable(Exception exception) {
        log.error("Exception occurred: {}", exception.getMessage(), exception);

        return errorResponseMapper.handleException(exception, DEFAULT_ERROR_CODE);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity handleValidationError(ConstraintViolationException exception) {
        log.error("Validation exception: {}", exception.getLocalizedMessage(), exception);
        ConstraintViolation constraintViolation = exception.getConstraintViolations().iterator().next();
        String messageTemplate = constraintViolation.getMessageTemplate();

        return errorResponseMapper.handleException(messageTemplate);
    }

    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity handleBusinessErrors(BusinessException exception) {
        log.error("Business exception: {}", exception.getMessage(), exception);

        return errorResponseMapper.handleException(exception.getErrorCode());
    }
}
