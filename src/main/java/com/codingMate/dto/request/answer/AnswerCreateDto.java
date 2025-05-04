package com.codingMate.dto.request.answer;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.answer.vo.LanguageType;
import lombok.Data;

@Data
public class AnswerCreateDto {
    private String code;
    private String title;
    private String explanation;
    private LanguageType languageType;
    private Long backjoonId;

    public Answer toEntity(){
        return Answer.builder()
                .code(code)
                .title(title)
                .explanation(explanation)
                .languageType(languageType)
                .backJoonId(backjoonId)
                .build();
    }
}