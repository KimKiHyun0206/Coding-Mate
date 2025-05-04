package com.codingMate.controller.ui.programmer;


import com.codingMate.dto.request.programmer.MyPateDto;
import com.codingMate.service.programmer.MyPageService;
import com.codingMate.service.programmer.ProgrammerService;
import com.codingMate.util.HeaderTokenUtil;
import com.codingMate.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
}