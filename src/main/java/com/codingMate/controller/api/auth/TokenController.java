package com.codingMate.controller.api.auth;

import com.codingMate.dto.request.token.RefreshTokenSaveRequest;
import com.codingMate.service.redis.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/refresh-token")
    public ResponseEntity<?> getUserLoginIdByRefreshToken(@RequestParam String refreshToken) {
        Long l = refreshTokenService.find(refreshToken);
        return ResponseEntity.ok(l);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> saveRefreshToken(@RequestBody RefreshTokenSaveRequest request) {
        Long save = refreshTokenService.save(request.getRefreshToken(), request.getUserLoginId());
        return ResponseEntity.ok(save);
    }
}