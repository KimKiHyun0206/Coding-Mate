package com.codingMate.dto.request.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import lombok.Data;

@Data
public class AnswerUpdateDto {
    private Long id;
    private String code;
    private String explanation;
    private LanguageType languageType;
}