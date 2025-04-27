package com.codingMate.dto.response.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.dto.response.comment.CommentDto;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnswerDto {
    private Long id;
    private Long backjoonId;
    private String answer;
    private String explanation;
    private Integer recommendation;
    private ProgrammerDto programmer;
    private LanguageType languageType;
}
