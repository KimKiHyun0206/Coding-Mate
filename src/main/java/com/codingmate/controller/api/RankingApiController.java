package com.codingmate.controller.api;

import com.codingmate.ranking.dto.RankingReadDto;
import com.codingmate.ranking.service.RankingService;
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

    @GetMapping
    public ResponseEntity<List<RankingReadDto>> getRanking(){
        return ResponseEntity.ok(rankingService.getRankingFromRedis());
    }

    @PostMapping
    public ResponseEntity<List<RankingReadDto>> refreshRanking(){
        return ResponseEntity.ok(rankingService.refreshRanking());
    }
}