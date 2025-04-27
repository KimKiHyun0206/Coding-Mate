package com.codingMate.dto.response.programmer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProgrammerRankingDto {
    private String name;
    private Long numberOfAnswer;
}
