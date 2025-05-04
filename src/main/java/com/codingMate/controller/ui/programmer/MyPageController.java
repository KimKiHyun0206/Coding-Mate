package com.codingMate.controller.ui.programmer;


import com.codingMate.service.programmer.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @RequestMapping("/my-page")
    public String myPage() {
        return "programmer/my_page";
    }

    @RequestMapping("/my-page/edit")
    public String myPageEdit() {
        return "programmer/my_page_edit";
    }
}