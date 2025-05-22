package com.codingMate.exception.exception.programmer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class NotFoundProgrammerException extends BusinessException {
    public NotFoundProgrammerException(ErrorMessage message) {
        super(message);
    }

    public NotFoundProgrammerException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public NotFoundProgrammerException(String reason) {
        super(reason);
    }
}