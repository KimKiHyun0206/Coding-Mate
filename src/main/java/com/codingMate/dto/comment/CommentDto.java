package com.codingMate.dto.comment;

import com.codingMate.dto.answer.AnswerDto;
import com.codingMate.dto.programmer.ProgrammerDto;
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