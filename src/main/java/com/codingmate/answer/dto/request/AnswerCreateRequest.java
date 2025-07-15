package com.codingmate.answer.dto.request;

import com.codingmate.answer.domain.vo.LanguageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Answer를 생성하기 위해 사용되는 DTO
 *
 * <ul>
 *     <li>code: 사용자가 작성한 풀이 코드</li>
 *     <li>title: 사용자가 작성한 풀이의 제목</li>
 *     <li>explanation: 사용자가 작성한 풀이에 대한 설명</li>
 *     <li>languageType: 사용자가 작성한 풀이의 언어</li>
 *     <li>backjoonId: 사용자가 작성한 풀이의 백준 번호</li>
 * </ul>
 *
 * @author duskafka
 *  */
@Schema(description = "Answer를 생성하기 위해 사용되는 DTO")
public record AnswerCreateRequest(
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