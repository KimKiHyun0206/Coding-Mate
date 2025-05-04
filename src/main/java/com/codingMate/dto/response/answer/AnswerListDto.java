package com.codingMate.dto.response.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.dto.response.programmer.SimpleProgrammerDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AnswerListDto {
    private Long answerId;
    private Long backjoonId;
    private String title;
    private String programmerName;
    private LanguageType language;

    @QueryProjection
    public AnswerListDto(Long answerId, Long backjoonId, String title, String programmerName, LanguageType language) {
        this.answerId = answerId;
        this.backjoonId = backjoonId;
        this.title = title;
        this.programmerName = programmerName;
        this.language = language;
    }
}
