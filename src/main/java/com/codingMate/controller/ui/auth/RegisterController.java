package com.codingMate.controller.ui.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    @RequestMapping
    public String register() {
        return "auth/register";
    }
}