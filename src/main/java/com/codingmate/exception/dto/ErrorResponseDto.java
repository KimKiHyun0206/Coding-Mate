package com.codingmate.exception.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponseDto {
    private final String code;
    private final String message;
    private final LocalDateTime serverDateTime;

    public static ResponseEntity<ErrorResponseDto> of(ErrorMessage message) {
        return ResponseEntity
                .status(message.getStatus())
                .body(new ErrorResponseDto(
                        message.getStatus().toString(),
                        message.getMessage(),
                        LocalDateTime.now())
                );
    }
}