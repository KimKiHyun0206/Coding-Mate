package com.codingMate.dto.response.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AnswerDto {
    private Long id;
    private Long backjoonId;
    private String title;
    private String code;
    private String explanation;
    private ProgrammerDto programmer;
    private LanguageType languageType;
}
