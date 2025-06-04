package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class NoTokenInHeaderException extends BusinessException {
    public NoTokenInHeaderException() {
        super(ErrorMessage.NO_TOKEN_IN_HEADER);
    }
}