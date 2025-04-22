package com.codingMate.exception.exception.recommend;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class ProgrammerNotRecommendAnswerException extends BusinessException {
    public ProgrammerNotRecommendAnswerException(Long programmerId, Long answerId) {
        super(ErrorMessage.PROGRAMMER_NOT_RECOMMEND_ANSWER, "programmerId : " + programmerId + " answerId : " + answerId);
    }
}