package com.codingMate.controller.ui.answer;

import com.codingMate.dto.response.answer.AnswerDto;
import com.codingMate.service.answer.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerListController {
    private final AnswerService answerService;

    @RequestMapping("/list")
    public String list(Model model) {
        List<AnswerDto> answerDtos = answerService.readAll();
        model.addAttribute("answers", answerDtos);
        return "auth/answer_list";
    }
}
