package com.codingMate.dto.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.domain.comment.Comment;
import com.codingMate.domain.programmer.Programmer;
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
    private Programmer programmer;
    private List<Comment> comment;
    private LanguageType languageType;
}
