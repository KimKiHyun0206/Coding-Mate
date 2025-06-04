package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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