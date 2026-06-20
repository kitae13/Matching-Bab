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
    ),

    USER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
        "사용자를 찾을 수 없습니다."
    ),

    EMAIL_NOT_VERIFIED(
            HttpStatus.BAD_REQUEST,
        "이메일 인증이 완료되지 않았습니다."
    ),

    NICKNAME_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
        "이미 사용 중인 닉네임입니다."
    ),

    INVALID_PASSWORD(
            HttpStatus.UNAUTHORIZED,
        "비밀번호가 일치하지 않습니다."
    ),

    INVALID_ROLE(
            HttpStatus.BAD_REQUEST,
        "선택할 수 없는 사용자 역할입니다."
    ),

    INTEREST_NOT_FOUND(
            HttpStatus.NOT_FOUND,
        "존재하지 않는 관심사가 포함되어 있습니다."
    ),

    FOOD_PREFERENCE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
        "존재하지 않는 음식 취향이 포함되어 있습니다."
    ),

    RESTRICTED_USER(
            HttpStatus.FORBIDDEN,
        "서비스 이용이 제한된 사용자입니다."
    ),

    INVALID_TOKEN(
            HttpStatus.UNAUTHORIZED,
        "유효하지 않은 인증 토큰입니다."
    ),

    EXPIRED_TOKEN(
            HttpStatus.UNAUTHORIZED,
        "인증 토큰이 만료되었습니다."
    ),

    INVALID_EMOJI_ID(
            HttpStatus.BAD_REQUEST,
            "올바르지 않은 이모티콘 ID입니다."
    ),

    TIMETABLE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "등록된 시간표가 없습니다."
    ),

    TIMETABLE_PRIVATE(
            HttpStatus.FORBIDDEN,
            "비공개 시간표입니다."
    ),

    INVALID_TIME_RANGE(
            HttpStatus.BAD_REQUEST,
            "시간표의 시작 시간은 종료 시간보다 빨라야 합니다."
    ),

    TIMETABLE_TIME_CONFLICT(
            HttpStatus.CONFLICT,
            "같은 요일에 시간이 겹치는 일정이 있습니다."
    ),

    MATCH_REQUEST_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "밥약 신청을 찾을 수 없습니다."
    ),

    CANNOT_REQUEST_SELF(
            HttpStatus.BAD_REQUEST,
            "본인에게 밥약을 신청할 수 없습니다."
    ),

    DIFFERENT_SCHOOL(
            HttpStatus.FORBIDDEN,
            "같은 학교 사용자에게만 밥약을 신청할 수 있습니다."
    ),

    INVALID_MATCH_TARGET(
            HttpStatus.BAD_REQUEST,
            "밥약을 신청할 수 없는 사용자입니다."
    ),

    ALREADY_REQUESTED(
            HttpStatus.CONFLICT,
            "이미 진행 중인 밥약 신청이 있습니다."
    ),

    INVALID_MATCH_STATUS(
            HttpStatus.BAD_REQUEST,
            "처리할 수 없는 밥약 신청 상태입니다."
    ),

    MATCH_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
            "이미 생성된 매칭입니다."
    ),

    CHATROOM_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
            "해당 매칭의 채팅방이 이미 존재합니다."
    ),

    CHATROOM_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "채팅방을 찾을 수 없습니다."
    ),;

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