package com.codingMate.dto.request.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import lombok.Data;

@Data
public class AnswerUpdateDto {
    private Long backjoonId;
    private String code;
    private String title;
    private String explanation;
    private LanguageType languageType;
}