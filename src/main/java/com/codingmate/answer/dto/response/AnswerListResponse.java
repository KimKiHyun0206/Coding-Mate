package com.codingmate.answer.dto.response;

import com.codingmate.answer.domain.vo.LanguageType;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 답변 목록 조회 시 각 항목에 사용되는 응답 DTO
 *
 * <li>answerId: 풀이의 PK로 이는 풀이를 확인하기 위해서 필요하다.</li>
 * <li>backjoonId: 풀이의 백준 ID</li>
 * <li>title: 풀이의 제목</li>
 * <li>programmerName: 작성한 유저의 이름</li>
 * <li>languageType: 풀이 코드의 프로그래밍 언어 타입</li>
 *
 * @author duskafka
 * */
@Schema(description = "답변 목록 조회 시 각 항목에 사용되는 응답 DTO")
public record AnswerListResponse(
        @Schema(description = "답변의 고유 ID", example = "1")
        Long answerId,

        @Schema(description = "이 답변이 참조하는 백준 문제의 ID", example = "1234")
        Long backjoonId,

        @Schema(description = "답변의 제목", example = "이번에 새로 작성한 1234번 풀이입니다.")
        String title,

        @Schema(description = "답변을 작성한 프로그래머의 이름", example = "홍길동")
        String programmerName,

        @Schema(description = "답변 코드의 프로그래밍 언어 타입", example = "JAVA")
        LanguageType languageType
) {
     @QueryProjection
     public AnswerListResponse(Long answerId, Long backjoonId, String title, String programmerName, LanguageType languageType) {
         this.answerId = answerId;
         this.backjoonId = backjoonId;
         this.title = title;
         this.programmerName = programmerName;
         this.languageType = languageType;
     }
}