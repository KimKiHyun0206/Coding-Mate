package com.codingMate.exception.exception.tip;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class NotFoundTipException extends BusinessException {
    public NotFoundTipException(Long tipId) {
        super(ErrorMessage.NOT_FOUND_TIP_EXCEPTION, "tipId : " + tipId);
    }
}