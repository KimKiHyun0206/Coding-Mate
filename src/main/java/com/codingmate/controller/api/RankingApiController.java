package com.codingmate.controller.api;

import com.codingmate.ranking.dto.SolveCountRankingDto;
import com.codingmate.ranking.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rankings")
@RequiredArgsConstructor
public class RankingApiController {

    private final RankingService rankingService;

    @Operation(summary = "Redis에 등록된 오늘의 랭킹을 읽어온다.", description = "Redis에 등록된 오늘의 랭킹을 읽어온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Redis에서 랭킹 조회 성공"),
    })
    @GetMapping
    public ResponseEntity<List<SolveCountRankingDto>> getRanking(){
        return ResponseEntity.ok(rankingService.getRankingFromRedis());
    }

    @Operation(summary = "Redis에 등록된 랭킹 재설정", description = "Redis에 등록된 랭킹을 재설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Redis에서 랭킹 재설정 성공"),
    })
    @PostMapping
    public ResponseEntity<List<SolveCountRankingDto>> refreshRanking(){
        return ResponseEntity.ok(rankingService.refreshRanking());
    }
}