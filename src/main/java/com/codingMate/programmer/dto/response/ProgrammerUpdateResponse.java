package com.codingMate.programmer.dto.response;

import com.codingMate.programmer.domain.Programmer;
import lombok.Builder;

@Builder
public record ProgrammerUpdateResponse(String githubId, String name, String email, String tip) {
    public static ProgrammerUpdateResponse from(Programmer programmer) {
        return ProgrammerUpdateResponse.builder()
                .githubId(programmer.getGithubId())
                .name(programmer.getName().getName())
                .email(programmer.getEmail().getEmail())
                .tip(programmer.getTip())
                .build();
    }
}