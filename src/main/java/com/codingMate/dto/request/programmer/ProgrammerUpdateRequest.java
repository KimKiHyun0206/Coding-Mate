package com.codingMate.dto.request.programmer;

public record ProgrammerUpdateRequest(String githubId, String name, String email, String tip) {
}