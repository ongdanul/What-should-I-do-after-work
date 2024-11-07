package com.elice.boardproject.users.controller;

import com.elice.boardproject.users.dto.SignUpDTO;
import com.elice.boardproject.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "user/signUp";
    }

    @PostMapping("/sign-up")
    public String signUpProcess(SignUpDTO dto, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        userService.signUpProcess(dto);

        redirectAttributes
                .addFlashAttribute("message", "환영합니다, " + dto.getUserName() + "님!");

        return "redirect:/user/login";
    }

}
