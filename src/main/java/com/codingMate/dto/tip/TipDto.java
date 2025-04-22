package com.codingMate.dto.tip;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TipDto {
    private Long id;
    private String content;
    private Long recommendation = 0L;
}
