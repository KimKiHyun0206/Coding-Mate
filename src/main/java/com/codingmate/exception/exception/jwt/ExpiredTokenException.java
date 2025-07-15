package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 요청에 있는 JWT가 유효기간이 지났을 때 발생하는 예외.
 *
 * <ul>
 *     <li>헤더에 담긴 Authorization: Bearer TOKEN 이 유효하지 않을 때 발생한다.</li>
 *     <li>이 경우 401을 응답하며 토큰 갱신 로직이 수행되도록 프론트 JS에서 구현해두었다.</li>
 * </ul>
 * */
public class ExpiredTokenException extends BusinessException {
    public ExpiredTokenException(ErrorMessage message) {
        super(message);
    }

    public ExpiredTokenException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public ExpiredTokenException(String reason) {
        super(reason);
    }
}