package com.codingMate.answer.dto.response;

import com.codingMate.answer.domain.vo.LanguageType;
import com.codingMate.programmer.dto.response.ProgrammerResponse;
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