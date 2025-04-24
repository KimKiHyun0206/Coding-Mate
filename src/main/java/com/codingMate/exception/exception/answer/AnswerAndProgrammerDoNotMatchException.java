package com.codingMate.exception.exception.answer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class AnswerAndProgrammerDoNotMatchException extends BusinessException {
    public AnswerAndProgrammerDoNotMatchException(Long programmerId, Long answerId) {
        super(ErrorMessage.ANSWER_AND_PROGRAMMER_DO_NOT_MATCH, "programmer id : " + programmerId + " answer id : " + answerId);
    }
}