package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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