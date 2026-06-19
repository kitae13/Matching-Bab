package com.example.matchingbab.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_INPUT_VALUE(
            HttpStatus.BAD_REQUEST,
            "입력값이 올바르지 않습니다."
    ),

    INVALID_JSON(
            HttpStatus.BAD_REQUEST,
            "요청 본문 형식이 올바르지 않습니다."
    ),

    UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            "인증이 필요합니다."
    ),

    FORBIDDEN(
            HttpStatus.FORBIDDEN,
            "해당 요청에 대한 권한이 없습니다."
    ),

    ENTITY_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "요청한 데이터를 찾을 수 없습니다."
    ),

    METHOD_NOT_ALLOWED(
            HttpStatus.METHOD_NOT_ALLOWED,
            "지원하지 않는 HTTP 메서드입니다."
    ),

    DUPLICATED_VALUE(
            HttpStatus.CONFLICT,
            "이미 존재하는 데이터입니다."
    ),

    SCHOOL_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "학교를 찾을 수 없습니다."
    ),

    INVALID_EMAIL_DOMAIN(
            HttpStatus.BAD_REQUEST,
            "선택한 학교의 이메일 도메인과 일치하지 않습니다."
    ),

    EMAIL_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
            "이미 가입된 이메일입니다."
    ),

    EMAIL_CODE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "이메일 인증번호 발송 기록을 찾을 수 없습니다."
    ),

    EMAIL_CODE_EXPIRED(
            HttpStatus.BAD_REQUEST,
            "이메일 인증번호가 만료되었습니다."
    ),

    EMAIL_CODE_NOT_MATCHED(
            HttpStatus.BAD_REQUEST,
            "이메일 인증번호가 일치하지 않습니다."
    ),

    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "서버 내부 오류가 발생했습니다."
    );

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}