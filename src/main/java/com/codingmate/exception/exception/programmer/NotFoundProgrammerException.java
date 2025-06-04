package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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