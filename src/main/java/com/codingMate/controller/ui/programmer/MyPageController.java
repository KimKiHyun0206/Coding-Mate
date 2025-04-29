package com.codingMate.controller.ui.programmer;


import com.codingMate.service.programmer.ProgrammerService;
import com.codingMate.util.HeaderTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/programmer")
@RequiredArgsConstructor
public class MyPageController {
    private final ProgrammerService programmerService;

    /*@RequestMapping
    public String myPage(Model model, HttpServletRequest request) {
        String token = HeaderTokenUtil.getTokenFromHeader(request);
    }*/
}
