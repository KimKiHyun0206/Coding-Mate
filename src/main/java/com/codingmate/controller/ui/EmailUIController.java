package com.codingmate.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailUIController {
    @GetMapping("/verification-result")
    public String showVerificationResult(@RequestParam("success") boolean success, Model model) {
        model.addAttribute("success", success);
        return "email/email_verification_result";
    }
}