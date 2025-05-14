package com.codingMate.exception.exception.programmer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class DuplicateProgrammerLoginIdException extends BusinessException {
    public DuplicateProgrammerLoginIdException() {
        super(ErrorMessage.DUPLICATE_PROGRAMMER_EXCEPTION);
    }
}