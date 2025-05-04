package com.codingMate.dto.request.programmer;

import lombok.Data;

@Data
public class ProgrammerUpdateRequest {
    private String loginId;
    private String githubLink;
    private String password;
    private String name;
    private String email;
}
