package com.codingmate.programmer.dto.response;

import com.codingmate.programmer.domain.Programmer;
import lombok.Builder;

@Builder
public record ProgrammerResponse(
        Long id,
        String loginId,
        String githubLink,
        String password,
        String name,
        String email,
        String tip,
        String authority) {
    public static ProgrammerResponse from(Programmer programmer) {
        return ProgrammerResponse.builder()
                .id(programmer.getId())
                .loginId(programmer.getLoginId())
                .githubLink(programmer.getGithubId())
                .password(programmer.getPassword())
                .name(programmer.getName().getName())
                .email(programmer.getEmail().getEmail())
                .tip(programmer.getTip())
                .authority(programmer.getAuthority().getAuthorityName())
                .build();
    }
}