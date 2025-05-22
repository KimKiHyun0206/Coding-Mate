package com.codingMate.answer.dto.request;

import com.codingMate.answer.domain.Answer;
import com.codingMate.answer.domain.vo.LanguageType;
import com.codingMate.programmer.domain.Programmer;

public record AnswerCreateRequest(String code, String title, String explanation, LanguageType languageType, Long backjoonId) {
    public Answer toEntity(Programmer programmer){
        return Answer.builder()
                .code(code)
                .title(title)
                .explanation(explanation)
                .languageType(languageType)
                .programmer(programmer)
                .backJoonId(backjoonId)
                .build();
    }
}