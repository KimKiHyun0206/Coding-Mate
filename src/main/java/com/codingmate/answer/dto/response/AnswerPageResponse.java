package com.codingmate.answer.dto.response;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.domain.vo.LanguageType;
import lombok.Builder;

import java.util.Objects;

@Builder
public record AnswerPageResponse(
        Long id,
        Long backjoonId,
        String title,
        String code,
        String explanation,
        String programmerName,
        Long programmerId,
        LanguageType languageType,
        Boolean isRequesterIsOwner) {

    public static AnswerPageResponse of(Answer answer, Long requestProgrammerId) {
        return AnswerPageResponse.builder()
                .id(answer.getId())
                .backjoonId(answer.getBackJoonId())
                .title(answer.getTitle())
                .code(answer.getCode())
                .explanation(answer.getExplanation())
                .programmerId(answer.getProgrammer().getId())
                .languageType(answer.getLanguageType())
                .programmerName(answer.getProgrammer().getName().getName())
                .isRequesterIsOwner(Objects.equals(requestProgrammerId, answer.getProgrammer().getId()))
                .build();
    }
}