package com.codingMate.answer.dto.response;

import com.codingMate.answer.domain.Answer;
import com.codingMate.answer.domain.vo.LanguageType;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
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