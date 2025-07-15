package com.codingmate.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

/**
 * 에러가 발생했을 때 GlobalExceptionHandler에서 응답에 사용하는 DTO
 *
 * @author duskafka
 * */
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