package com.codingmate.answer.dto.request;

import com.codingmate.answer.domain.vo.LanguageType;


public record AnswerUpdateRequest(
        Long backjoonId,
        String code,
        String title,
        String explanation,
        LanguageType languageType)
{}