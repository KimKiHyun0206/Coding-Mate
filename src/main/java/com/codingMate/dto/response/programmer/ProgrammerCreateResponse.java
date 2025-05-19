package com.codingMate.dto.response.programmer;

import com.codingMate.domain.programmer.Programmer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgrammerCreateResponse {
    private String githubLink;
    private String name;
    private String email;
    private String tip;

    public static ProgrammerCreateResponse from(String githubLink, String name, String email, String tip) {
        return ProgrammerCreateResponse.builder()
                .githubLink(githubLink)
                .name(name)
                .email(email)
                .tip(tip)
                .build();
    }

    public static ProgrammerCreateResponse from(Programmer programmer) {
        return ProgrammerCreateResponse.builder()
                .githubLink(programmer.getGithubId())
                .name(programmer.getName().getName())
                .email(programmer.getEmail().getEmail())
                .tip(programmer.getTip())
                .build();
    }
}
