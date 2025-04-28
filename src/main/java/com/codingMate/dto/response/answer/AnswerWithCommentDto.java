package com.codingMate.dto.response.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.dto.response.programmer.SimpleProgrammerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerWithCommentDto {
    private Long backjoonId;
    private String code;
    private String explanation;
    private SimpleProgrammerDto programmer;
    private LanguageType languageType;
}