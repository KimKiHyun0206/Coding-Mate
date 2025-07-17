package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class UnMatchJTIException extends BusinessException {
    public UnMatchJTIException(ErrorMessage message) {
        super(message);
    }

    public UnMatchJTIException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public UnMatchJTIException(String reason) {
        super(reason);
    }
}
