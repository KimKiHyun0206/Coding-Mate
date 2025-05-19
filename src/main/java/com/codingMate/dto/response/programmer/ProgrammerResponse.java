package com.codingMate.dto.response.programmer;

import com.codingMate.domain.programmer.Programmer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgrammerResponse {
    private Long id;
    private String loginId;
    private String githubLink;
    private String password;
    private String name;
    private String email;
    private String tip;
    private String authority;

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