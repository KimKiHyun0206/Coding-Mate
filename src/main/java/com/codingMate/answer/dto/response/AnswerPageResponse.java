package com.codingMate.answer.dto.response;

import com.codingMate.answer.domain.Answer;
import com.codingMate.answer.domain.vo.LanguageType;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
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