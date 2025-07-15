package com.codingmate.exception.exception.ranking;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * {@code JobBuilder}가 잘못된 파라미터를 생성했을 때 발생하는 예외.
 * */
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