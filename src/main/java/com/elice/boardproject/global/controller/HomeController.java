package com.elice.boardproject.global.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            return "redirect:/board/list";
        }
        return "user/login";
    }

    @GetMapping("/notice")
    public String notice() {
        return "global/notice";
    }
}
