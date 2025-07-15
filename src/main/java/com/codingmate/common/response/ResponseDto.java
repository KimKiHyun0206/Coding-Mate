package com.codingmate.common.response;

import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 응답에 일관성을 가질 수 있도록 하는 DTO
 *
 * <li>data: 응답 body</li>
 * <li>message: 응답 메시지</li>
 * <li>serverDateTime: 서버 응답 시간</li>
 *
 * @author duskafka
 * */
@Data
public class ResponseDto<T> {
    private final T data;
    private final String message;
    private final String serverDateTime;

    public ResponseDto(ResponseMessage message, T data) {
        this.message = message.name();
        this.serverDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.data = data;
    }

    public static <T> ResponseEntity<ResponseDto<T>> toResponseEntity(ResponseMessage message, T data) {
        return ResponseEntity
                .status(message.getStatus())
                .body(new ResponseDto<>(message, data));
    }

    public static <T> ResponseEntity<ResponseDto<?>> toResponseEntity(ResponseMessage message) {
        return ResponseEntity
                .status(message.getStatus())
                .body(null);
    }
}