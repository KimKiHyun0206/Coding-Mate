package com.codingmate.programmer.dto.response;

import com.codingmate.programmer.domain.Programmer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
/**
 * 프로그래머 정보 수정에 성공했을 때 응답하는 DTO
 *
 * <li>ADMIN API 에서만 사용해야 함</li>
 * <li>githubId: 사용자의 깃허브 아이디</li>
 * <li>name: 사용자의 이름</li>
 * <li>email: 사용자의 이메일</li>
 * <li>tip: 사용자가 작성한 팁</li>
 *
 *
 * @author duskafka
 * */
@Builder
@Schema(description = "프로그래머 정보 업데이트 성공 응답 DTO")
public record ProgrammerUpdateResponse(
        @Schema(description = "업데이트된 GitHub ID (없을 수 있음)", example = "updated_user_github", nullable = true)
        String githubId,

        @Schema(description = "업데이트된 이름 또는 닉네임", example = "새로운이름")
        String name,

        @Schema(description = "업데이트된 이메일 주소", example = "updated_email@example.com")
        String email,

        @Schema(description = "업데이트된 한 줄 팁 또는 소개 (없을 수 있음)", example = "새로운 팁입니다!", nullable = true)
        String tip
) {
    /**
     * Programmer 엔티티를 사용하여 ProgrammerUpdateResponse를 생성합니다.
     *
     * @param programmer 업데이트된 정보를 가진 Programmer 엔티티
     * @return ProgrammerUpdateResponse 객체
     */
    public static ProgrammerUpdateResponse of(Programmer programmer) {
        return ProgrammerUpdateResponse.builder()
                .githubId(programmer.getGithubId())
                .name(programmer.getName().getName())
                .email(programmer.getEmail().getEmail())
                .tip(programmer.getTip())
                .build();
    }
}