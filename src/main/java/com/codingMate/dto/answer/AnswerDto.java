package com.codingMate.dto.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.domain.comment.Comment;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.comment.CommentDto;
import com.codingMate.dto.programmer.ProgrammerDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnswerDto {
    private Long id;
    private String answer;
    private String explanation;
    private Integer recommendation;
    private ProgrammerDto programmer;
    private List<CommentDto> comment;
    private LanguageType languageType;
}
