package com.codingMate.dto.response.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
public class AnswerPageDto {
    private Long id;
    private Long backjoonId;
    private String title;
    private String code;
    private String explanation;
    private String programmerName;
    private LanguageType languageType;
    private Boolean isRequesterIsOwner;
}