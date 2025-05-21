package com.codingMate.programmer.dto.request;

public record ProgrammerCreateRequest(
        String loginId,
        String githubId,
        String password,
        String name,
        String email)
{}