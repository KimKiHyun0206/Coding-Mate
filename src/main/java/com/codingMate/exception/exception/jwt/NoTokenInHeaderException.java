package com.codingMate.exception.exception.jwt;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class NoTokenInHeaderException extends BusinessException {
    public NoTokenInHeaderException() {
        super(ErrorMessage.NO_TOKEN_IN_HEADER);
    }
}
