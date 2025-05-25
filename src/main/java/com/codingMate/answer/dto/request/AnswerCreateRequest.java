package com.codingMate.answer.dto.request;

import com.codingMate.answer.domain.vo.LanguageType;

public record AnswerCreateRequest(
        String code,
        String title,
        String explanation,
        LanguageType languageType,
        Long backjoonId) {
}