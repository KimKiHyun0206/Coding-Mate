package com.codingmate.answer.dto.response;

import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.programmer.dto.response.ProgrammerResponse;
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