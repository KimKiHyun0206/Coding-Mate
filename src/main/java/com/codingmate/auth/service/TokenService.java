package com.codingmate.auth.service;

import com.codingmate.auth.dto.response.TokenDto;
import com.codingmate.jwt.TokenProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenService {
    private final TokenProvider tokenProvider;

    public TokenDto generateToken(Long id, String authority) {
        return new TokenDto(
                tokenProvider.createAccessToken(id, authority),
                tokenProvider.createRefreshToken(id)
        );
    }

    public void validateToken(String token){
        tokenProvider.validateToken(token);
    }
}