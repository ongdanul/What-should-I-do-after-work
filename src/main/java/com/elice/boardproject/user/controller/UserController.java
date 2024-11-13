package com.elice.boardproject.user.controller;

import com.elice.boardproject.user.dto.SignUpDTO;
import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String login(HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            return "redirect:/board/list";
        }
        return "user/login";
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "user/signUp";
    }

    @PostMapping("/sign-up")
    public String signUpProcess(@Valid SignUpDTO dto, RedirectAttributes redirectAttributes) {

        userService.signUpProcess(dto);

        redirectAttributes
                .addFlashAttribute("message", "환영합니다, " + dto.getUserName() + "님!");

        return "redirect:/user/login";
    }

    @GetMapping("/find-id")
    public String findId() {
        return "user/findId";
    }

    @PostMapping("/find-id")
    public ResponseEntity<String> findId(HttpServletRequest request) {

        String userName = request.getParameter("userName");
        String contact = request.getParameter("contact");

        String userId = userService.findUser(userName, contact);

        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/find-pw")
    public String findPw() {
        return "user/findPw";
    }

    @PostMapping("/find-pw")
    public ResponseEntity<String> findPw(HttpServletRequest request) {

        String userName = request.getParameter("userName");
        String email = request.getParameter("email");

        String resultMessage = userService.findPw(userName, email);

        if ("비밀번호가 이메일로 발송되었습니다.".equals(resultMessage)) {
            return ResponseEntity.ok(resultMessage);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMessage);
        }
    }
}
