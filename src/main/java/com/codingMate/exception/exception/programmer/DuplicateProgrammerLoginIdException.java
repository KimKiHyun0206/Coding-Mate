package com.codingMate.exception.exception.programmer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class DuplicateProgrammerLoginIdException extends BusinessException {
    public DuplicateProgrammerLoginIdException(ErrorMessage message) {
        super(message);
    }

    public DuplicateProgrammerLoginIdException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public DuplicateProgrammerLoginIdException(String reason) {
        super(reason);
    }
}