package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class RefreshTokenOverMax extends BusinessException {
    public RefreshTokenOverMax(ErrorMessage message) {
        super(message);
    }

    public RefreshTokenOverMax(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public RefreshTokenOverMax(String reason) {
        super(reason);
    }
}
