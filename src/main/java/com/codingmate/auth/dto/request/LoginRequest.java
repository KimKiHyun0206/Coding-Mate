package com.codingmate.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 로그인 요청을 위한 DTO
 * <li>loginId: ID</li>
 * <li>password: PW</li>
 *
 * @author duskafka
 * */
@Schema(description = "사용자 로그인 요청 DTO")
public record LoginRequest(
        @Schema(description = "사용자 로그인 ID", example = "codingmate")
        @NotBlank(message = "로그인 ID는 필수 값입니다.")
        String loginId,

        @Schema(description = "사용자 비밀번호", example = "password123!")
        @NotBlank(message = "비밀번호는 필수 값입니다.")
        String password
) {
}