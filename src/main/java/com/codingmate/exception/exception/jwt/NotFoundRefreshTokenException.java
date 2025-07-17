package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class NotFoundRefreshTokenException extends BusinessException {
    public NotFoundRefreshTokenException(ErrorMessage message) {
        super(message);
    }

    public NotFoundRefreshTokenException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public NotFoundRefreshTokenException(String reason) {
        super(reason);
    }
}
