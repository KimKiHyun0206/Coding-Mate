package com.codingMate.programmer.domain.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum RoleType {

    USER("USER", "유저"),
    ADMIN("ADMIN", "관리자");

    private final String role;
    private final String value;
}