package com.codingmate.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 서버 응답 상태 코드를 가지는 열거형
 *
 * @author duskafka
 * */
@Getter
public enum ResponseMessage {

    SUCCESS(HttpStatus.OK, "SUCCESS"),
    CREATED(HttpStatus.CREATED, "CREATED"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
    NO_CONTENT(HttpStatus.NO_CONTENT, "NO_CONTENT"),
    AUTHORIZED(HttpStatus.OK, "AUTHORIZED"),
    //PROGRAMMER
    ;

    public final static String SUCCESS_MESSAGE = "SUCCESS";
    private final static String NOT_FOUND_MESSAGE = "NOT FOUND";


    private final HttpStatus status;
    private final String message;

    ResponseMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}