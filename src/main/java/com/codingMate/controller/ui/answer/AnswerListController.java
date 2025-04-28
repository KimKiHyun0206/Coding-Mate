package com.codingMate.controller.ui.answer;

import com.codingMate.dto.response.answer.AnswerDto;
import com.codingMate.dto.response.answer.AnswerListDto;
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
        List<AnswerListDto> answerListDtos = answerService.readAnswerlist();
        model.addAttribute("answers", answerListDtos);
        return "answer/answer_list";
    }
}
