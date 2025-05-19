package com.codingMate.dto.response.answer;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.answer.vo.LanguageType;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.Objects;

@Data
@Builder
public class AnswerPageResponse {
    private Long id;
    private Long backjoonId;
    private String title;
    private String code;
    private String explanation;
    private String programmerName;
    private Long programmerId;
    private LanguageType languageType;
    private Boolean isRequesterIsOwner = false;

    public static AnswerPageResponse from(Answer answer) {
        return AnswerPageResponse.builder()
                .id(answer.getId())
                .backjoonId(answer.getBackJoonId())
                .title(answer.getTitle())
                .code(answer.getCode())
                .explanation(answer.getExplanation())
                .programmerId(answer.getProgrammer().getId())
                .languageType(answer.getLanguageType())
                .programmerName(answer.getProgrammer().getName().getName())
                .build();
    }

    public void setIsRequesterIsOwner(Long programmerId){
        this.isRequesterIsOwner = Objects.equals(this.programmerId, programmerId);
    }
}