package com.codingMate.dto.request.answer;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.answer.vo.LanguageType;

public record AnswerCreateRequest(String code, String title, String explanation, LanguageType languageType, Long backjoonId) {
    public Answer toEntity(){
        return Answer.builder()
                .code(code)
                .title(title)
                .explanation(explanation)
                .languageType(languageType)
                .backJoonId(backjoonId)
                .build();
    }
}