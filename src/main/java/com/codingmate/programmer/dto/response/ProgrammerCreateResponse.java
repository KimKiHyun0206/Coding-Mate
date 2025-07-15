package com.codingmate.programmer.dto.response;

import com.codingmate.programmer.domain.Programmer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 새로운 사용자가 생성되었음을 응답하는 DTO
 *
 * <li>ADMIN API 에서만 사용해야 함</li>
 * <li>githubId: 사용자의 깃허브 아이디</li>
 * <li>name: 사용자의 이름</li>
 * <li>email: 사용자의 이메일</li>
 * <li>tip: 사용자의 팁</li>
 *
 * @author duskafka
 * */
@Builder
@Schema(description = "새로운 프로그래머 계정 생성 성공 응답 DTO")
public record ProgrammerCreateResponse(
        @Schema(description = "생성된 프로그래머의 GitHub 프로필 ID (GitHub 링크를 구성하는 데 사용될 수 있음)", example = "new_user_github", nullable = true)
        String githubId,

        @Schema(description = "생성된 프로그래머의 이름 또는 닉네임", example = "새로운코더")
        String name,

        @Schema(description = "생성된 프로그래머의 이메일 주소", example = "new_user@example.com")
        String email,

        @Schema(description = "생성된 프로그래머의 한 줄 팁 또는 소개 (없을 수 있음)", example = "즐거운 코딩 라이프!", nullable = true)
        String tip
) {
    /**
     * Programmer 엔티티를 사용하여 ProgrammerCreateResponse를 생성합니다.
     *
     * @param programmer 생성된 Programmer 엔티티
     * @return ProgrammerCreateResponse 객체
     */
    public static ProgrammerCreateResponse of(Programmer programmer) {
        return ProgrammerCreateResponse.builder()
                .githubId(programmer.getGithubId())
                .name(programmer.getName().getName())
                .email(programmer.getEmail().getEmail())
                .tip(programmer.getTip())
                .build();
    }
}