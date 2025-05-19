package com.codingMate.dto.response.programmer;

import com.codingMate.domain.programmer.Programmer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgrammerUpdateResponse {
    public String githubId;
    public String name;
    public String email;
    public String tip;

    public static ProgrammerUpdateResponse from(Programmer programmer) {
        return ProgrammerUpdateResponse.builder()
                .githubId(programmer.getGithubId())
                .name(programmer.getName().getName())
                .email(programmer.getEmail().getEmail())
                .tip(programmer.getTip())
                .build();
    }
}
