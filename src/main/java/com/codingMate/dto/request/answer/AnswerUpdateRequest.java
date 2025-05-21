package com.codingMate.dto.request.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import lombok.Data;


public record AnswerUpdateRequest(
        Long backjoonId,
        String code,
        String title,
        String explanation,
        LanguageType languageType)
{}