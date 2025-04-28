package com.codingMate.controller.ui.answer;

import com.codingMate.dto.response.answer.AnswerWithCommentDto;
import com.codingMate.service.answer.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller("/answer")
@RequiredArgsConstructor
public class AnswerPageController {
    private final AnswerService answerService;

    @RequestMapping("/{id}")
    public String answerPage(@PathVariable(name = "id") Long id, Model model) {
        AnswerWithCommentDto answerWithCommentDto = answerService.answerPageLoadService(id);
        model.addAttribute("answerWithComment", answerWithCommentDto);
        return "answer/answer";
    }
}
