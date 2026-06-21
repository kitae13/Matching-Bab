package com.example.matchingbab.global.response;

import com.example.matchingbab.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(
        boolean success,
        String errorCode,
        String message,
        Map<String, String> errors
) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                false,
                errorCode.name(),
                errorCode.getMessage(),
                Collections.emptyMap()
        );
    }

    public static ErrorResponse of(
            ErrorCode errorCode,
            String message
    ) {
        return new ErrorResponse(
                false,
                errorCode.name(),
                message,
                Collections.emptyMap()
        );
    }

    public static ErrorResponse of(
            ErrorCode errorCode,
            Map<String, String> errors
    ) {
        return new ErrorResponse(
                false,
                errorCode.name(),
                errorCode.getMessage(),
                errors
        );
    }
}