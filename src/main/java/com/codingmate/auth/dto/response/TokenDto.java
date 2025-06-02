package com.codingmate.auth.dto.response;

import lombok.Builder;

@Builder
public record TokenDto(String accessToken ,String refreshToken) {
    public static TokenDto of(String accessToken, String refreshToken){
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}