package com.codingmate.answer.dto.request;

import com.codingmate.answer.domain.vo.LanguageType;

public record AnswerCreateRequest(
        String code,
        String title,
        String explanation,
        LanguageType languageType,
        Long backjoonId) {
}