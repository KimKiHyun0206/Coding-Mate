package com.codingmate.programmer.dto.response;

import com.codingmate.programmer.domain.Programmer;
import lombok.Builder;

@Builder
public record ProgrammerCreateResponse(String githubLink, String name, String email, String tip) {
    public static ProgrammerCreateResponse of(Programmer programmer) {
        return ProgrammerCreateResponse.builder()
                .githubLink(programmer.getGithubId())
                .name(programmer.getName().getName())
                .email(programmer.getEmail().getEmail())
                .tip(programmer.getTip())
                .build();
    }
}