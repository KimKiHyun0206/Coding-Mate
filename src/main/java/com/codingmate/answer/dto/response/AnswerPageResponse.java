package com.codingmate.answer.dto.response;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.domain.vo.LanguageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Objects;

@Builder
@Schema(description = "단일 답변 조회 또는 답변 목록 페이지 조회 시 사용되는 응답 DTO")
public record AnswerPageResponse(
        @Schema(description = "조회된 답변의 고유 ID", example = "1")
        Long id,

        @Schema(description = "답변의 코드 내용", example = "System.out.println(\"Hello, World!\");")
        String code,

        @Schema(description = "이 답변이 참조하는 백준 문제의 ID", example = "1234")
        Long backjoonId,

        @Schema(description = "답변의 제목", example = "이번에 새로 작성한 1234번 문제 풀이입니다.")
        String title,

        @Schema(description = "답변에 대한 추가 설명 (선택 사항)", example = "이 코드는 Hello World를 출력하는 간단한 자바 코드입니다.")
        String explanation,

        @Schema(description = "답변을 작성한 프로그래머의 이름", example = "홍길동")
        String programmerName,

        @Schema(description = "답변 코드의 프로그래밍 언어 타입", example = "JAVA")
        LanguageType languageType,

        @Schema(description = "답변을 작성한 프로그래머의 고유 ID", example = "1")
        Long programmerId,

        @Schema(description = "해당 답변이 받은 추천 수", example = "100")
        Integer likeCount,

        @Schema(description = "요청자가 이 답변의 소유자인지 여부", example = "true")
        Boolean isRequesterIsOwner,

        @Schema(description = "요청자가 이 답변에 추천을 눌렀는지 여부", example = "true")
        Boolean isLiked
) {
        /**
         * Answer 엔티티와 추가 정보를 사용하여 AnswerPageResponse를 생성합니다.
         *
         * @param answer              응답으로 변환할 Answer 엔티티
         * @param requestProgrammerId 현재 요청을 보낸 프로그래머의 ID (로그인하지 않은 경우 null)
         * @param isLiked             현재 요청을 보낸 프로그래머가 이 답변을 추천했는지 여부
         * @return AnswerPageResponse 객체
         */
        public static AnswerPageResponse of(Answer answer, Long requestProgrammerId, Boolean isLiked) {
                return AnswerPageResponse.builder()
                        .id(answer.getId())
                        .backjoonId(answer.getBackJoonId())
                        .title(answer.getTitle())
                        .code(answer.getCode())
                        .explanation(answer.getExplanation())
                        .likeCount(answer.getLikeCount())
                        .programmerId(answer.getProgrammer().getId())
                        .languageType(answer.getLanguageType())
                        .programmerName(answer.getProgrammer().getName().getName())
                        .isRequesterIsOwner(Objects.equals(requestProgrammerId, answer.getProgrammer().getId()))
                        .isLiked(isLiked)
                        .build();
        }
}