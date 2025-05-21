package com.codingMate.answer.dto.request;

import com.codingMate.answer.domain.vo.LanguageType;


public record AnswerUpdateRequest(
        Long backjoonId,
        String code,
        String title,
        String explanation,
        LanguageType languageType)
{}