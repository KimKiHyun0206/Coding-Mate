package com.codingMate.exception.exception.comment;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class NotFoundCommentException extends BusinessException {
    public NotFoundCommentException(Long commendId) {
        super(ErrorMessage.NOT_FOUND_COMMENT_EXCEPTION, "commentId :" + commendId);
    }
}
