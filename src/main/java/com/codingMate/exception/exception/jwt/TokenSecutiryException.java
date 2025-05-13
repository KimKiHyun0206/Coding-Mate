package com.codingMate.exception.exception.jwt;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class TokenSecutiryException extends BusinessException {

    public TokenSecutiryException(ErrorMessage message) {
        super(message);
    }

    public TokenSecutiryException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public TokenSecutiryException(String reason) {
        super(reason);
    }
}