package com.codingMate.dto.request.programmer;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LoginDto {
    private String loginId;
    private String password;
}
