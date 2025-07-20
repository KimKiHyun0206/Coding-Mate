package com.codingmate.exception.exception.email;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 데이터베이스에서 EmailVerificationToken 엔티티를 조회하지 못했을 때 발생하는 예외.
 *
 * <li>EmailService에서 token값으로 조회에 실패했을 시에 발생한다.</li>
 * */
public class NotFoundEmailVerificationTokenException extends BusinessException {
    public NotFoundEmailVerificationTokenException(ErrorMessage message) {
        super(message);
    }

    public NotFoundEmailVerificationTokenException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public NotFoundEmailVerificationTokenException(String reason) {
        super(reason);
    }
}