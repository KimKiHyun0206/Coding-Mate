package com.codingMate.dto.response.programmer;

import com.codingMate.domain.programmer.Programmer;
import lombok.Builder;

@Builder
public record ProgrammerCreateResponse(String githubLink, String name, String email, String tip) {
    public static ProgrammerCreateResponse from(Programmer programmer) {
        return ProgrammerCreateResponse.builder()
                .githubLink(programmer.getGithubId())
                .name(programmer.getName().getName())
                .email(programmer.getEmail().getEmail())
                .tip(programmer.getTip())
                .build();
    }
}