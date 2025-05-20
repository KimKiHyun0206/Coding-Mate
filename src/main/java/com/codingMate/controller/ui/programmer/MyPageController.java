package com.codingMate.controller.ui.programmer;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyPageController {
    @RequestMapping("/my-page")
    public String myPage() {
        return "programmer/my_page";
    }

    @RequestMapping("/my-page/edit")
    public String myPageEdit() {
        return "programmer/my_page_edit";
    }

    @RequestMapping("/my-page/answer")
    public String myAnswer(){
        return "programmer/my_page_answer";
    }
}