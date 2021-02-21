package com.example.demo.frontend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("message", message);
        return "welcome";
    }

    @GetMapping("/patients")
    public String patientList(Model model) {
        return "patientList";
    }
}
