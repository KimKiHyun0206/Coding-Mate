package com.codingMate.dto.response.programmer;

import com.codingMate.domain.programmer.Programmer;
import lombok.Builder;

@Builder
public record MyPageResponse(String githubId, String name, String email, Long numberOfAnswer, String tip) {
    public static MyPageResponse from(Programmer programmer) {
        return MyPageResponse.builder()
                .githubId(programmer.getGithubId())
                .email(programmer.getEmail().getEmail())
                .name(programmer.getName().getName())
                .tip(programmer.getTip())
                .build();
    }
}