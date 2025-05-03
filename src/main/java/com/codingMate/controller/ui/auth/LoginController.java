package com.codingMate.controller.ui.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    @RequestMapping
    public String login() {
        return "auth/login";
    }
}