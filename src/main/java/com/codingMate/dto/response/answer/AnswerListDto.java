package com.codingMate.dto.response.answer;

import com.codingMate.dto.response.programmer.SimpleProgrammerDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AnswerListDto {
    private Long backjoonId;
    private String title;
    private String programmerName;

    @QueryProjection
    public AnswerListDto(Long backjoonId, String title, String programmerName) {
        this.backjoonId = backjoonId;
        this.title = title;
        this.programmerName = programmerName;
    }
}
