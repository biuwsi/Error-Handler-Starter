package com.biuwsi.common.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DefaultBaseErrorCodes implements ExceptionErrorCode {
    NULL_POINTER_EXCEPTION("error.null.pointer"),

    UNKNOWN_ERROR("error.unknown"),
    REQUEST_FAILED("error.request.failed");

    private final String code;
}
