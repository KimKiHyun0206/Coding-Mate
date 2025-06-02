package com.codingmate.answer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AnswerCreateResponse(
        @Schema(description = "생성된 Answer의 ID", example = "1")
        Long id
) {
}