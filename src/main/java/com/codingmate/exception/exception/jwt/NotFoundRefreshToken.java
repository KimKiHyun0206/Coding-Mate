package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class NotFoundRefreshToken extends BusinessException {
    public NotFoundRefreshToken(ErrorMessage message) {
        super(message);
    }

    public NotFoundRefreshToken(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public NotFoundRefreshToken(String reason) {
        super(reason);
    }
}
