package com.codingmate.exception.exception.ranking;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class JobBuilderBuildInvalidParametersException extends BusinessException {
    public JobBuilderBuildInvalidParametersException(ErrorMessage message) {
        super(message);
    }

    public JobBuilderBuildInvalidParametersException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public JobBuilderBuildInvalidParametersException(String reason) {
        super(reason);
    }
}