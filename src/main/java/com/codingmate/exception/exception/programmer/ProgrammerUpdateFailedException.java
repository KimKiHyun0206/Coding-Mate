package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class ProgrammerUpdateFailedException extends BusinessException {
    public ProgrammerUpdateFailedException(ErrorMessage message) {
        super(message);
    }

    public ProgrammerUpdateFailedException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public ProgrammerUpdateFailedException(String reason) {
        super(reason);
    }
}