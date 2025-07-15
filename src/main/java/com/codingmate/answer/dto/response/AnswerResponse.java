package com.codingmate.answer.dto.response;

import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.programmer.dto.response.ProgrammerResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 단일 답변 조회 시 사용되는 응답 DTO
 *
 * <li>ADMIN API에서만 사용해야 한다.</li>
 * <li>id: 풀이의 PK</li>
 * <li>backjoonId: 풀이의 백준 번호</li>
 * <li>title: 풀이의 제목</li>
 * <li>code: 풀이의 코드</li>
 * <li>explanation: 풀이 설명</li>
 * <li>programmerResponse: 풀이를 작성한 사용자에 대한 정보</li>
 * <li>languageType: 풀이의 언어 타입</li>
 *
 * @author duskafka
 * */
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