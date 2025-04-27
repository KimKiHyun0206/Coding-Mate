package com.codingMate.controller.ui.ranking;

import com.codingMate.dto.response.programmer.ProgrammerRankingDto;
import com.codingMate.service.home.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/ranking")
@RequiredArgsConstructor
public class RankingController {
    private final RankingService rankingService;

    @RequestMapping
    public String ranking(Model model){
        List<ProgrammerRankingDto> rank = rankingService.getRank();
        model.addAttribute("rank", rank);
        return "ranking";
    }
}
