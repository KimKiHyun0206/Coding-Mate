package com.codingMate.dto.comment;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.programmer.Programmer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String comment;
    private Integer recommendation;
    private Answer answer;
    private Programmer programmer;
}