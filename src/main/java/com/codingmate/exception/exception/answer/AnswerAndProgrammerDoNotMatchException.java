package com.codingmate.exception.exception.answer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 풀이를 작성한 사용자와 요청한 사용자가 일치하지 않을 때 발생하는 오류.
 *
 * <ul>
 *     <li>프로트맨 같은 API 만 따로 보내는 것을 사용해 유효하지 않은 요청을 보냈을 때 이를 감지하고 처리해주는 예외</li>
 *     <li>HTML 클라이언트에서는 작성자가 아니면 수정 또는 삭제 버튼이 보이지 않으므로 이는 악의적인 공격 및 실수에 의한 것을 방지할 수 있음.</li>
 * </ul>
 * */
public class AnswerAndProgrammerDoNotMatchException extends BusinessException {
    public AnswerAndProgrammerDoNotMatchException(ErrorMessage message) {
        super(message);
    }

    public AnswerAndProgrammerDoNotMatchException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public AnswerAndProgrammerDoNotMatchException(String reason) {
        super(reason);
    }
}