package com.elice.boardproject.user.controller;

import com.elice.boardproject.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    /**
     * 로그인 페이지 - 사용자가 이미 로그인되어 있다면, 게시판 리스트 페이지로 리다이렉트됩니다.
     * @param request HTTP 요청 객체
     * @return 로그인 페이지 또는 게시판 리스트 페이지로 리다이렉트
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            return "redirect:/board/list";
        }
        return "user/login";
    }

    /**
     * 회원가입 페이지
     * @return 회원가입 페이지
     */
    @GetMapping("/sign-up")
    public String signUp() {
        return "user/signUp";
    }

//    @PostMapping("/sign-up")
//    public String signUpProcess(@Valid SignUpDTO signUpDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "user/signUp";
//        }
//
//        userService.signUpProcess(signUpDTO);
//
//        redirectAttributes
//                .addFlashAttribute("message", "환영합니다, " + signUpDTO.getUserName() + "님!");
//
//        return "redirect:/user/login";
//    }

    /**
     * 아이디 찾기 페이지
     * @return 아이디 찾기 페이지
     */
    @GetMapping("/find-id")
    public String findId() {
        return "user/findId";
    }

    /**
     * 비밀번호 찾기 페이지
     * @return 비밀번호 찾기 페이지
     */
    @GetMapping("/find-pw")
    public String findPw() {
        return "user/findPw";
    }

}
