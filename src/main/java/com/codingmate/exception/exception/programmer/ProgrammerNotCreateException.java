package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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
