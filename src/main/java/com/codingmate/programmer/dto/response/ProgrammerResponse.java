package com.codingmate.programmer.dto.response;

import com.codingmate.programmer.domain.Programmer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "프로그래머 상세 정보 응답 DTO")
public record ProgrammerResponse(
        @Schema(description = "프로그래머의 고유 ID", example = "1")
        Long id,

        @Schema(description = "프로그래머의 로그인 ID", example = "my_awesome_coder")
        String loginId,

        @Schema(description = "프로그래머의 GitHub 프로필 ID (GitHub 링크를 구성하는 데 사용될 수 있음)", example = "github_profile_name", nullable = true)
        String githubId,

        @Schema(description = "프로그래머의 이름 또는 닉네임", example = "코딩메이트")
        String name,

        @Schema(description = "프로그래머의 이메일 주소", example = "coder@example.com")
        String email,

        @Schema(description = "프로그래머의 한 줄 팁 또는 소개 (없을 수 있음)", example = "꾸준히 배우고 성장하는 개발자입니다!", nullable = true)
        String tip,

        @Schema(description = "프로그래머의 권한 (예: ROLE_USER, ROLE_ADMIN)", example = "ROLE_USER")
        String authority
) {
    /**
     * Programmer 엔티티를 사용하여 ProgrammerResponse를 생성합니다.
     * 보안을 위해 비밀번호 정보는 포함하지 않습니다.
     *
     * @param programmer 상세 정보를 조회할 Programmer 엔티티
     * @return ProgrammerResponse 객체
     */
    public static ProgrammerResponse of(Programmer programmer) {
        return ProgrammerResponse.builder()
                .id(programmer.getId())
                .loginId(programmer.getLoginId())
                .githubId(programmer.getGithubId()) // Programmer 엔티티의 getGithubId()를 그대로 사용
                // .password(programmer.getPassword()) // 보안상 이 라인은 제거해야 합니다.
                .name(programmer.getName().getName())
                .email(programmer.getEmail().getEmail())
                .tip(programmer.getTip())
                .authority(programmer.getAuthority().getAuthorityName())
                .build();
    }
}