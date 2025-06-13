package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class UnMatchJTI extends BusinessException {
    public UnMatchJTI(ErrorMessage message) {
        super(message);
    }

    public UnMatchJTI(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public UnMatchJTI(String reason) {
        super(reason);
    }
}
