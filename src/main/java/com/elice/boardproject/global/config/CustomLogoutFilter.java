package com.elice.boardproject.global.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {

    /**
     * 필터를 수행하여 로그아웃 요청을 처리합니다.
     * 요청이 로그아웃 경로와 POST 방식일 때만 로그아웃을 수행하고, 그 외의 경우 필터 체인을 계속 진행합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param chain    필터 체인
     * @throws IOException 예외 발생 시
     * @throws ServletException 예외 발생 시
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }


    /**
     * 로그아웃 요청이 POST 방식인 경우에만 로그아웃을 수행하고, 쿠키를 삭제한 뒤 홈 페이지로 리다이렉트합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인
     * @throws IOException 예외 발생 시
     * @throws ServletException 예외 발생 시
     */
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

        // 리프레시 토큰 확인
        String refresh = CookieUtils.getCookieValue(request, "refresh_token");
        // refresh 토큰이 없는 경우 (일반 로그인 처리)
        if (refresh == null) {
            CookieUtils.clearLocalLoginCookies(response);
        } else {
            // refresh 토큰이 있는 경우 (소셜 로그인 처리)
            CookieUtils.clearSocialLoginCookies(response);
        }

        // Spring Security 인증 상태를 제거
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);

        response.setStatus(HttpServletResponse.SC_OK);
        redirectToHome(response, request);
    }

    /**
     * 로그아웃 후 사용자 홈 화면으로 리다이렉트합니다.
     *
     * @param response HTTP 응답 객체
     * @param request HTTP 요청 객체
     */
    private void redirectToHome(HttpServletResponse response, HttpServletRequest request) {
        try {
            response.sendRedirect(request.getContextPath() + "/user/login");
        } catch (IOException e) {
            log.error("IOException occurred while redirecting", e);
        }
    }
}
