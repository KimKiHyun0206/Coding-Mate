package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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
