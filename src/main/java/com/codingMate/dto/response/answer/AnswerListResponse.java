package com.codingMate.dto.response.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AnswerListResponse {
    private Long answerId;
    private Long backjoonId;
    private String title;
    private String programmerName;
    private LanguageType languageType;

    @QueryProjection
    public AnswerListResponse(Long answerId, Long backjoonId, String title, String programmerName, LanguageType languageType) {
        this.answerId = answerId;
        this.backjoonId = backjoonId;
        this.title = title;
        this.programmerName = programmerName;
        this.languageType = languageType;
    }
}