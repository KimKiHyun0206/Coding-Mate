package com.codingMate.common.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PageDto<T> {
    private final List<T> data;
    private final int page;
    private final int size;
    private final long totalElements;

    public static <T> ResponseEntity<PageDto<T>> ok(Page<T> data) {
        var response = new PageDto<T>(
                data.getContent(),
                data.getPageable().getPageNumber(),
                data.getPageable().getPageSize(),
                data.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }
}