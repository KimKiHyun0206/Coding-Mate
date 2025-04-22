package com.codingMate.dto.response.tip;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TipDto {
    private Long id;
    private String content;
    private Long recommendation;
}
