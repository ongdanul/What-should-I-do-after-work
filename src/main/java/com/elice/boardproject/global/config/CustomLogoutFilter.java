package com.elice.boardproject.global.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 로그아웃 요청 경로 확인
        String requestUri = request.getRequestURI();

        if (!requestUri.matches("^/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 로그아웃 요청은 POST 방식이어야 한다.
        String requestMethod = request.getMethod();

        if (!requestMethod.equals("POST")) {
            log.warn("This is not a POST method");
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);

        clearCookies(response);

        response.setStatus(HttpServletResponse.SC_OK);
        redirectToHome(response, request);
    }

    // 쿠키 삭제 메소드
    private void clearCookies(HttpServletResponse response) {
        // 자동 로그인 쿠키 삭제
        Cookie rememberMeCookie = new Cookie("remember-me", null);
        rememberMeCookie.setMaxAge(0);
        rememberMeCookie.setPath("/");

        // 아이디 저장 쿠키 삭제
//        Cookie rememberIdCookie = new Cookie("rememberId", null);
//        rememberIdCookie.setMaxAge(0);
//        rememberIdCookie.setPath("/");

        response.addCookie(rememberMeCookie);
//        response.addCookie(rememberIdCookie);
    }

    // 홈 화면으로 리다이렉트
    private void redirectToHome(HttpServletResponse response, HttpServletRequest request) {
        try {
            response.sendRedirect(request.getContextPath() + "/");
        } catch (IOException e) {
            log.error("IOException occurred while redirecting", e);
        }
    }
}
