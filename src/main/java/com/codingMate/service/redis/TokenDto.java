package com.codingMate.service.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}