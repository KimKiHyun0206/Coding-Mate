package com.codingMate.dto.request.programmer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class LoginRequest {
    private String loginId;
    private String password;
}
