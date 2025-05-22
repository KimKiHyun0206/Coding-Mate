package com.codingMate.exception.exception.programmer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class ProgrammerNotCreateException extends BusinessException {
    public ProgrammerNotCreateException(ErrorMessage message) {
        super(message);
    }

    public ProgrammerNotCreateException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public ProgrammerNotCreateException(String reason) {
        super(reason);
    }
}
