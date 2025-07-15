package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 유효하지 않은 토큰이 요청에 들어왔을 때 발생하는 예외.
 *
 * <ul>
 *     <li>{@code IllegalArgumentException}가 감지되었을 때 발생한다.</li>
 * </ul>
 * */
public class IllegalTokenException extends BusinessException {
    public IllegalTokenException(ErrorMessage message) {
        super(message);
    }

    public IllegalTokenException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public IllegalTokenException(String reason) {
        super(reason);
    }
}