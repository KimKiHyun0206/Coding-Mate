package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 이미 사용된 리프래쉬 토큰일 때 발생하는 예외이다.
 *
 * <ul>
 *     <li>토큰 블랙리스트 구현에서 사용.</li>
 *     <li>이 오류가 발생하면 토큰이 탈취당하여 액세스 토큰과 리프래쉬 토큰을 갱신하는 데 사용되었다는 의미.</li>
 * </ul>
 * */
public class RefreshTokenIsRevoked extends BusinessException {
    public RefreshTokenIsRevoked(ErrorMessage message) {
        super(message);
    }

    public RefreshTokenIsRevoked(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public RefreshTokenIsRevoked(String reason) {
        super(reason);
    }
}
