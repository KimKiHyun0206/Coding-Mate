package com.codingmate.programmer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "새로운 프로그래머 계정 생성 요청 DTO")
public record ProgrammerCreateRequest(
        @Schema(description = "로그인 시 사용할 사용자 ID (중복 불가능)", example = "new_coder123")
        @NotBlank(message = "로그인 ID는 필수 값입니다.")
        String loginId,

        @Schema(description = "사용자의 GitHub ID (선택 사항)", example = "github_user_name")
        String githubId, // GitHub ID는 필수가 아닐 수 있으므로 @NotBlank 제거

        @Schema(description = "사용자 비밀번호", example = "Password!123")
        @NotBlank(message = "비밀번호는 필수 값입니다.")
        String password,

        @Schema(description = "사용자의 이름 (실명 또는 닉네임)", example = "김코딩")
        @NotBlank(message = "이름은 필수 값입니다.")
        String name,

        @Schema(description = "사용자 이메일 주소 (중복 불가능)", example = "user@example.com")
        @NotBlank(message = "이메일은 필수 값입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email
) {
}