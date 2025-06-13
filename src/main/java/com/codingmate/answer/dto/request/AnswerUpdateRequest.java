package com.codingmate.answer.dto.request;

import com.codingmate.answer.domain.vo.LanguageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Answer를 수정하기 위해 필요한 DTO")
public record AnswerUpdateRequest(
        @Schema(description = "코드 내용", example = "System.out.println(\"Hello\");")
        @NotBlank(message = "코드는 필수 값입니다.")
        String code,

        @Schema(description = "제목", example = "문제 풀이 코드")
        @NotBlank(message = "제목은 필수 값입니다.")
        String title,

        @Schema(description = "설명", example = "이 코드는 Hello World를 출력합니다.")
        String explanation,

        @Schema(description = "언어 타입", example = "JAVA", required = true)
        @NotNull(message = "언어 타입은 필수 값입니다.")
        LanguageType languageType,

        @Schema(description = "백준 문제 ID", example = "1000", required = true)
        @NotNull(message = "백준 ID는 필수 값입니다.")
        Long backjoonId) {
}