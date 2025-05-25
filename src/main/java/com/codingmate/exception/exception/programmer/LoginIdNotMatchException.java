package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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
