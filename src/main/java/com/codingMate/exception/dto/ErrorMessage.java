package com.codingMate.exception.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMessage {
    //Server
    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 못한 에러가 발생했습니다"),

    //PROGRAMMER
    DUPLICATE_PROGRAMMER_EXCEPTION(HttpStatus.CONFLICT, "중복된 PROGRAMMER 가 존재합니다"),
    NOT_FOUND_PROGRAMMER_EXCEPTION(HttpStatus.NOT_FOUND, "요청한 PROGRAMMER 를 찾을 수 없습니다"),
    INVALID_PASSWORD_REGEX(HttpStatus.CONFLICT, "유효한 비밀번호 형식이 아닙니다"),
    INVALID_EMAIL_REGEX(HttpStatus.CONFLICT, "유효한 이메일 형식이 아닙니다"),
    INVALID_NAME_REGEX(HttpStatus.CONFLICT, "유효한 이름 형식이 아닙니다"),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "ID/PW 가 일치하지 않습니다"),
    WRONG_ID(HttpStatus.BAD_REQUEST, "ID 가 일치하지 않습니다"),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "Password 가 일치하지 않습니다"),

    //ANSWER
    NOT_FOUND_ANSWER_EXCEPTION(HttpStatus.NOT_FOUND, "요청한 ANSWER 를 찾을 수 없습니다"),
    ANSWER_AND_PROGRAMMER_DO_NOT_MATCH(HttpStatus.BAD_REQUEST, "요청한 ANSWER는 요청한 PROGRAMMER의 ANSWER가 아닙니다"),

    //COMMENT
    NOT_FOUND_COMMENT_EXCEPTION(HttpStatus.NOT_FOUND, "요청한 COMMENT 를 찾을 수 없습니다"),

    //TIP
    NOT_FOUND_TIP_EXCEPTION(HttpStatus.NOT_FOUND, "요청한 TIP 을 찾을 수 없습니다"),

    //RECOMMEND
    PROGRAMMER_NOT_RECOMMEND_ANSWER(HttpStatus.NOT_FOUND, "해당 PROGRAMMER 는 요청한 ANSWER 를 추천하지 않았습니다"),
    PROGRAMMER_NOT_RECOMMEND_COMMENT(HttpStatus.NOT_FOUND, "해당 PROGRAMMER 는 요청한 COMMENT 를 추천하지 않았습니다"),
    PROGRAMMER_NOT_RECOMMEND_TIP(HttpStatus.NOT_FOUND, "해당 PROGRAMMER 는 요청한 TIP 를 추천하지 않았습니다"),

    //JWT
    INVALID_JWT(HttpStatus.UNAUTHORIZED,"유효하지 않은 JWT 토큰입니다"),


    //Redis
    REDIS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Redis Server에서 오류가 발생했습니다"),


    ;


    private final HttpStatus status;
    private final String message;

    ErrorMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    }