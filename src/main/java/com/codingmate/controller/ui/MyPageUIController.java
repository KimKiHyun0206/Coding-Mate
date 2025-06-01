package com.codingmate.controller.ui;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyPageUIController {
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
        return "programmer/answer_list_paging";
    }
}