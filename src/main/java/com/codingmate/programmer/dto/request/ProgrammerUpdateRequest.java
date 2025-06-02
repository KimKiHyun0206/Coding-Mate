package com.codingmate.programmer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "프로그래머 정보 업데이트 요청 DTO")
public record ProgrammerUpdateRequest(
        @Schema(description = "업데이트할 GitHub ID (선택 사항)", example = "updated_github_user", nullable = true)
        String githubId,

        @Schema(description = "업데이트할 이름 (선택 사항)", example = "김코딩", nullable = true)
        String name,

        @Schema(description = "업데이트할 이메일 주소 (선택 사항)", example = "update@example.com", nullable = true)
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @Schema(description = "프로그래머의 팁/소개 (선택 사항)", example = "안녕하세요! 꾸준히 코딩하는 개발자입니다.", nullable = true)
        String tip
) {
}