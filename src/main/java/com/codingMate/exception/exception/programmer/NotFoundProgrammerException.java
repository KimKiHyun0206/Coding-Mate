package com.codingMate.exception.exception.programmer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class NotFoundProgrammerException extends BusinessException {
    public NotFoundProgrammerException(Long programmerId) {
        super(ErrorMessage.NOT_FOUND_PROGRAMMER_EXCEPTION, "programmer.id : " + programmerId);
    }
    public NotFoundProgrammerException(String loginId) {
        super(ErrorMessage.NOT_FOUND_PROGRAMMER_EXCEPTION, "programmer.loginId : " + loginId);
    }
}