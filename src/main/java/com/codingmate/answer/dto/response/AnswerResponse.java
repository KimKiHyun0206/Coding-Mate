package com.codingmate.answer.dto.response;

import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.programmer.dto.response.ProgrammerResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "단일 답변 조회 시 사용되는 응답 DTO")
public record AnswerResponse(
        @Schema(description = "답변의 고유 ID", example = "1")
        Long id,

        @Schema(description = "이 답변이 참조하는 백준 문제의 ID", example = "1234")
        Long backjoonId,

        @Schema(description = "답변의 제목", example = "이번에 새로 작성한 1234번 문제 풀이입니다.")
        String title,

        @Schema(description = "답변의 코드 내용", example = "System.out.println(\"Hello, World!\");")
        String code,

        @Schema(description = "답변에 대한 추가 설명 (선택 사항)", example = "이 코드는 Hello World를 출력하는 간단한 자바 코드입니다.")
        String explanation,

        @Schema(description = "답변을 작성한 프로그래머 정보")
        ProgrammerResponse programmerResponse,

        @Schema(description = "답변 코드의 프로그래밍 언어 타입", example = "JAVA")
        LanguageType languageType
) {
}