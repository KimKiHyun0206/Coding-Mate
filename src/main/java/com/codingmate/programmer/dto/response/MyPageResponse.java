package com.codingmate.programmer.dto.response;

import com.codingmate.programmer.domain.Programmer;
import lombok.Builder;

import com.codingmate.programmer.domain.Programmer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "사용자의 마이페이지 정보 응답 DTO")
public record MyPageResponse(
        @Schema(description = "사용자의 GitHub ID (없을 수 있음)", example = "mygithubid", nullable = true)
        String githubId,

        @Schema(description = "사용자의 이름 또는 닉네임", example = "코딩메이트")
        String name,

        @Schema(description = "사용자의 이메일 주소", example = "user@codingmate.com")
        String email,

        @Schema(description = "사용자가 작성한 총 답변 수", example = "15")
        Long numberOfAnswer,

        @Schema(description = "사용자의 한 줄 팁 또는 소개 (없을 수 있음)", example = "꾸준함이 답입니다!", nullable = true)
        String tip
) {
    /**
     * Programmer 엔티티와 작성한 답변 수를 사용하여 MyPageResponse를 생성합니다.
     *
     * @param programmer   마이페이지 정보를 조회할 Programmer 엔티티
     * @param numberOfAnswer 해당 프로그래머가 작성한 총 답변 수
     * @return MyPageResponse 객체
     */
    public static MyPageResponse of(Programmer programmer, Long numberOfAnswer) {
        return MyPageResponse.builder()
                .githubId(programmer.getGithubId())
                .email(programmer.getEmail().getEmail())
                .name(programmer.getName().getName())
                .tip(programmer.getTip())
                .numberOfAnswer(numberOfAnswer)
                .build();
    }
}