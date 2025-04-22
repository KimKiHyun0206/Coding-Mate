package com.codingMate.domain.programmer.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {

    USER("USER", "유저"),
    ADMIN("ADMIN", "관리자");

    private final String role;
    private final String value;
}