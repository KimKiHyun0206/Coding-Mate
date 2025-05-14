package com.codingMate.dto.request.token;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenSaveRequest {
    private Long userLoginId;
    private String refreshToken;
}