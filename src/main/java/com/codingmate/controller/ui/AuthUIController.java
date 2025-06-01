package com.codingmate.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class AuthUIController {
    @RequestMapping("/login")
    public String login() {
        return "auth/login";
    }

    @RequestMapping("/register")
    public String register() {
        return "auth/register";
    }
}