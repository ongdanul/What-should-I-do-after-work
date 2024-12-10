package com.elice.boardproject.global.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 홈 컨트롤러 클래스입니다.
 * 애플리케이션의 홈 페이지 및 기타 주요 페이지로의 요청을 처리합니다.
 */
@Controller
public class HomeController {

    /**
     * 홈페이지 요청을 처리합니다.
     * 사용자가 로그인 상태인 경우 게시판 목록 페이지로 리다이렉트합니다.
     * 사용자가 로그인하지 않은 경우 로그인 페이지로 이동합니다.
     *
     * @param request HTTP 요청 객체
     * @return 리다이렉트 또는 뷰 이름
     */
    @GetMapping("/")
    public String home(HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            return "redirect:/board/list";
        }
        return "user/login";
    }

    /**
     * 공지사항 페이지
     *
     * @return 공지사항 페이지 뷰 이름
     */
    @GetMapping("/notice")
    public String notice() {
        return "global/notice";
    }
}
