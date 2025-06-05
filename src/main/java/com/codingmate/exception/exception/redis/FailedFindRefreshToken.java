package com.codingmate.exception.exception.redis;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class FailedFindRefreshToken extends BusinessException {
    public FailedFindRefreshToken(ErrorMessage message) {
        super(message);
    }

    public FailedFindRefreshToken(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public FailedFindRefreshToken(String reason) {
        super(reason);
    }
}
