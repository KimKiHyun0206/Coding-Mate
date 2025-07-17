package com.codingmate.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 에러가 발생했을 때 HTTP 상태 코드와 메시지를 반환하는 열거형
 *
 * <li>열거형의 접미어로 Exception을 붙이지 않는다.</li>
 * <li>네이밍 일관성을 가지게 한다. NOT_FOUND_XXX, INVALID_XXX, FAILED_XXX, DUPLICATE_XXX 등으로 통일</li>
 *
 * @author duskafka
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorMessage {
    //Server
    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 못한 에러가 발생했습니다."),

    //PROGRAMMER
    DUPLICATE_PROGRAMMER(HttpStatus.CONFLICT, "중복된 사용자가 존재합니다."),
    PROGRAMMER_NOT_CREATED(HttpStatus.BAD_REQUEST, "사용자 생성에 실패했습니다."),
    INVALID_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 ID입니다."),
    NOT_FOUND_PROGRAMMER(HttpStatus.NOT_FOUND, "요청한 사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD_REGEX(HttpStatus.CONFLICT, "유효한 비밀번호 형식이 아닙니다."),
    INVALID_EMAIL_REGEX(HttpStatus.CONFLICT, "유효한 이메일 형식이 아닙니다."),
    INVALID_NAME_REGEX(HttpStatus.CONFLICT, "유효한 이름 형식이 아닙니다."),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "ID/PW 가 일치하지 않습니다."),
    WRONG_ID(HttpStatus.BAD_REQUEST, "ID 가 일치하지 않습니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "Password 가 일치하지 않습니다"),
    UPDATE_FAILED(HttpStatus.NOT_FOUND, "업데이트에 실패했습니다."),

    //ANSWER
    NOT_FOUND_ANSWER(HttpStatus.NOT_FOUND, "풀이를 찾을 수 없습니다."),
    ANSWER_AND_PROGRAMMER_DO_NOT_MATCH(HttpStatus.BAD_REQUEST, "해당 풀이는 사용자가 작성한 풀이가 아닙니다."),

    //JWT
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    NO_TOKEN_IN_HEADER(HttpStatus.UNAUTHORIZED, "헤더에 토큰이 없습니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "토큰 유효기간이 만료되었습니다."),
    SECURITY_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다."),
    REFRESH_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, "갱신할 리프레쉬 토큰이 없습니다."),
    UN_MATCH_JTI(HttpStatus.BAD_REQUEST, "JTI가 일치하지 않습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "요청한 리프레시 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_OVER_MAX(HttpStatus.CONFLICT, "리프레시 토큰을 더이상 발급할 수 없습니다."),
    REFRESH_TOKEN_REVOKED(HttpStatus.CONFLICT, "무효화된 리프레쉬 토큰입니다."),

    //RANKING
    NO_RANKING(HttpStatus.NO_CONTENT, "금일 랭킹이 존재하지 않습니다."),
    JOB_ALREADY_EXECUTION(HttpStatus.INTERNAL_SERVER_ERROR, "이미 스케줄러가 실행 중입니다."),
    JOB_BUILDER_BUILD_INVALID_PARAMETERS(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 Job 파라미터가 생성되었습니다."),
    RANKING_ILLEGAL_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "Rank 데이터 타입이 유효하지 않습니다."),
    RANKING_COUNT(HttpStatus.INTERNAL_SERVER_ERROR, "랭킹 카운트 처리 중 예외가 발생했습니다."),
    FAILED_SAVE_RANKING_IN_REDIS(HttpStatus.INTERNAL_SERVER_ERROR, "Redis에 저장하지 못했습니다."),

    //Redis
    REDIS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 서버 오류가 발생했습니다."),
    FAILED_DELETE_REFRESH_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "Redis에서 리프레시 토큰을 삭제하는 중 오류가 발생했습니다."),
    FAILED_FIND_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Redis에서 리프레시 토큰을 찾지 못했습니다."),
    ;


    private final HttpStatus status;
    private final String message;
}