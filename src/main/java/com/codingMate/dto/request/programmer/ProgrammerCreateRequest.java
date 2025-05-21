package com.codingMate.dto.request.programmer;

public record ProgrammerCreateRequest(
        String loginId,
        String githubId,
        String password,
        String name,
        String email)
{}