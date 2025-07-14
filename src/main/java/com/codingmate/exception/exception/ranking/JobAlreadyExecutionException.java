package com.codingmate.exception.exception.ranking;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class JobAlreadyExecutionException extends BusinessException {
    public JobAlreadyExecutionException(ErrorMessage message) {
        super(message);
    }

    public JobAlreadyExecutionException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public JobAlreadyExecutionException(String reason) {
        super(reason);
    }
}