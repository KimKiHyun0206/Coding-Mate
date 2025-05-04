package com.codingMate.dto.request.programmer;

import lombok.Data;

@Data
public class ProgrammerUpdateRequest {
    private String githubId;
    private String name;
    private String email;
    private String tip;
}
