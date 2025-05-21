package com.codingMate.programmer.dto.request;

public record ProgrammerUpdateRequest(String githubId, String name, String email, String tip) {
}