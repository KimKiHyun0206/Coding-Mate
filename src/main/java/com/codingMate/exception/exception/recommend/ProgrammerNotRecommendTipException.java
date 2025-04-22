package com.codingMate.exception.exception.recommend;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class ProgrammerNotRecommendTipException extends BusinessException {
    public ProgrammerNotRecommendTipException(Long programmerId, Long tipId) {
        super(ErrorMessage.PROGRAMMER_NOT_RECOMMEND_TIP, "programmerId : " + programmerId + ", tipId : " + tipId);
    }
}