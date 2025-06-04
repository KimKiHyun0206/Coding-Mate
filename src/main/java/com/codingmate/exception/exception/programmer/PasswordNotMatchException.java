package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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
