package com.codingMate.dto.response.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.dto.response.programmer.ProgrammerResponse;
import lombok.Builder;

@Builder
public record AnswerResponse(
        Long id,
        Long backjoonId,
        String title,
        String code,
        String explanation,
        ProgrammerResponse programmerResponse,
        LanguageType languageType)
{}