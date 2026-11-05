package com.career.guidance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendForwardController {

    @GetMapping(value = {
        "/",
        "/dashboard",
        "/careers",
        "/skill-gap",
        "/roadmap",
        "/assessments",
        "/interview",
        "/resume",
        "/admin"
    })
    public String forwardFrontendRoutes() {
        return "forward:/index.html";
    }
}
