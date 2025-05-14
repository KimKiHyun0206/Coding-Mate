package com.codingMate.exception.exception.programmer;

import com.codingMate.exception.BusinessException;

public class ProgrammerNotCreateException extends BusinessException {
    public ProgrammerNotCreateException(String message) {
        super(message);
    }
}
