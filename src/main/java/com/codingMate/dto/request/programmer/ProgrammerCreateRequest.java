package com.codingMate.dto.request.programmer;

import lombok.Data;

@Data
public class ProgrammerCreateRequest {
    private String loginId;
    private String githubId;
    private String password;
    private String name;
    private String email;
}