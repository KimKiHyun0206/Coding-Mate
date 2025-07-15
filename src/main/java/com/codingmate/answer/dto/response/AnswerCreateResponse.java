package com.codingmate.answer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 사용자가 생성한 풀이의 PK를 전달하기 위한 DTO
 *
 * <li>리다이렉션에 필요해서 리턴한다.</li>
 *
 * @author duskafka
 * */
public record AnswerCreateResponse(
        @Schema(description = "생성된 Answer의 ID", example = "1")
        Long id
) {
}