package com.codingMate.dto.response.comment;

import com.codingMate.dto.response.answer.AnswerDto;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String comment;
    private Integer recommendation;
    private AnswerDto answer;
    private ProgrammerDto programmer;
}