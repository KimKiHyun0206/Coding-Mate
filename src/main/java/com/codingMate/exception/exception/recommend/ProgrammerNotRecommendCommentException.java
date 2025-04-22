package com.codingMate.exception.exception.recommend;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class ProgrammerNotRecommendCommentException extends BusinessException {
    public ProgrammerNotRecommendCommentException(Long programmerId, Long commentId) {
        super(ErrorMessage.PROGRAMMER_NOT_RECOMMEND_COMMENT, "programmerId : " + programmerId + " commentId : " + commentId);
    }
}