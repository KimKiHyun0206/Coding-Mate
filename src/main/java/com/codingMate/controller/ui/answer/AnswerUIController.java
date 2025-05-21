package com.codingMate.controller.ui.answer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/answer")
public class AnswerUIController {

    @RequestMapping("/list")
    public String list() {
        return "answer/list_paging";
    }

    @RequestMapping("/{id}")
    public String answerPage(@PathVariable(name = "id") Long id) {
        log.info("answerPage({})", id);
        return "answer/answer";
    }

    @RequestMapping("/write")
    public String write(){
        return "answer/write";
    }

    @RequestMapping("/edit/{answerId}")
    public String edit(){
        return "answer/edit";
    }
}