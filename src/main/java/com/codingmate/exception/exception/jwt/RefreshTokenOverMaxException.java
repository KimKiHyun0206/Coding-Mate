package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 발급 가능한 리프래쉬 토큰 수가 제한되어 더이상 발급할 수 없을 때 발생하는 예외.
 *
 * <ul>
 *     <li>이 애플리케이션에서는 리프래쉬 토큰 발급 가능한 갯수를 3개로 제한한다.</li>
 *     <li>따라서 3개보다 더 많은 기기에서 로그인하려고 시도할 경우 로그인이 제한된다.</li>
 * </ul>
 * */
public class RefreshTokenOverMaxException extends BusinessException {
    public RefreshTokenOverMaxException(ErrorMessage message) {
        super(message);
    }

    public RefreshTokenOverMaxException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public RefreshTokenOverMaxException(String reason) {
        super(reason);
    }
}
