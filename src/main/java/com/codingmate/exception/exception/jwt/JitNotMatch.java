package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 리프래쉬 토큰 블랙리스트 구현에 jti 가 일치하지 않을 때 발생하는 예외.
 *
 * <ul>
 *     <li>리프래쉬 토큰 블랙리스트를 위해 jti를 사용.</li>
 *     <li>이전에 발급한 jti가 사용자가 토큰 갱신을 요청할 때 사용한 jti가 맞지 않을 때 발생한다.</li>
 * </ul>
 * */
public class JitNotMatch extends BusinessException {
    public JitNotMatch(ErrorMessage message) {
        super(message);
    }

    public JitNotMatch(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public JitNotMatch(String reason) {
        super(reason);
    }
}
