package com.codingMate.exception.exception.programmer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class LoginIdNotMatchException extends BusinessException {
    public LoginIdNotMatchException(ErrorMessage message) {
        super(message);
    }

    public LoginIdNotMatchException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public LoginIdNotMatchException(String reason) {
        super(reason);
    }
}
