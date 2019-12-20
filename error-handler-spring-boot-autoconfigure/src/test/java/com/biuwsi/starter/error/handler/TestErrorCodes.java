package com.biuwsi.starter.error.handler;

import com.biuwsi.common.exceptions.ExceptionErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TestErrorCodes implements ExceptionErrorCode {

    TEST_ERROR_CODE("test.error.code"),
    TEST_ERROR_MISSING_MESSAGES("test.error.missing.messages");

    private final String code;
}
