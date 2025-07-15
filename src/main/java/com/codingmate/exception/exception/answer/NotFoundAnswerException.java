package com.codingmate.exception.exception.answer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 요청한 풀이를 찾을 수 없을 때 발생한다.
 *
 * <ul>
 *     <li>대표적으로 {@code answer_id}가 유효하지 않을 때 발생한다.</li>
 *     <li>HTML을 사용한 브라우저 클라이언트를 사용하면 마주할 일이 없지만, 포스트맨 등을 사용할 때 마주할 수 있다.</li>
 * </ul>
 * */
public class NotFoundAnswerException extends BusinessException {
    public NotFoundAnswerException(ErrorMessage message) {
        super(message);
    }

    public NotFoundAnswerException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public NotFoundAnswerException(String reason) {
        super(reason);
    }
}