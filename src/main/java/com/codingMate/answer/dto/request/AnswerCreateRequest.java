package com.codingMate.answer.dto.request;

import com.codingMate.answer.domain.Answer;
import com.codingMate.answer.domain.vo.LanguageType;

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