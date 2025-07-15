package com.codingmate.exception;

import com.codingmate.exception.dto.ErrorMessage;
import lombok.Getter;

/**
 * 애플리케이션에서 발생하는 예외들을 래핑해서 {@code GlobalExceptionHandler}에서 차리하기 위한 예외.
 *
 * <ul>
 *     <li>애플리케이션에서 발생할 수 있는 모든 예외는 이 클래스를 상속한다.</li>
 * </ul>
 * */
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorMessage errorMessage;

    /**
     * ErrorMessage를 사용하여 예외를 생성합니다.
     *
     * @param message 오류 메시지
     * */
    public BusinessException(ErrorMessage message) {
        super(message.getMessage());
        this.errorMessage = message;
    }

    /**
     * ErrorMessage와 추가 이유를 사용하여 예외를 생성합니다.
     *
     * @param message 오휴 메시지
     * @param reason 추가 이유
     * */
    public BusinessException(ErrorMessage message, String reason) {
        super(reason);
        this.errorMessage = message;
    }

    /**
     * 이유만으로 예외를 생성한다.
     *
     * @param reason 예외 발생 이유
     * */
    public BusinessException(String reason) {
        super(reason);
        this.errorMessage = ErrorMessage.INTERNAL_SERVER_ERROR;
    }
}