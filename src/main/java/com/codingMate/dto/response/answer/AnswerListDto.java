package com.codingMate.dto.response.answer;

import com.codingMate.dto.response.programmer.SimpleProgrammerDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AnswerListDto {
    private Long answerId;
    private Long backjoonId;
    private String programmerName;

    @QueryProjection
    public AnswerListDto(Long answerId, Long backjoonId, String programmerName) {
        this.answerId = answerId;
        this.backjoonId = backjoonId;
        this.programmerName = programmerName;
    }
}
