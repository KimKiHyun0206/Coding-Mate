package com.codingMate.exception.exception.programmer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class PasswordNotMatchException extends BusinessException {
    public PasswordNotMatchException(ErrorMessage message) {
        super(message);
    }

    public PasswordNotMatchException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public PasswordNotMatchException(String reason) {
        super(reason);
    }
}
