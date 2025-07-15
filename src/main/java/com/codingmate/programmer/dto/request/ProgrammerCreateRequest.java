package com.codingmate.programmer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 사용자 생성을 위한 DTO
 *
 * <li>loginId: 사용자가 사용할 로그인 아이디</li>
 * <li>githubId: 사용자의 깃허브 아이디</li>
 * <li>password: 사용자의 비밀번호로 데이터베이스에는 암호화되어 저장</li>
 * <li>name: 사용자의 이름</li>
 * <li>email: 사용자의 이메일</li>
 *
 * @author duskafka
 * */
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