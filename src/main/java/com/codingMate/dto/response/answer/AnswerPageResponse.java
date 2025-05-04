package com.codingMate.dto.response.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerPageResponse {
    private Long id;
    private Long backjoonId;
    private String title;
    private String code;
    private String explanation;
    private String programmerName;
    private LanguageType languageType;
    private Boolean isRequesterIsOwner;
}