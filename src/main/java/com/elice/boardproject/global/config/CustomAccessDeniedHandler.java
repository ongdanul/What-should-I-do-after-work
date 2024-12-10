package com.elice.boardproject.global.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 접근이 거부된 경우 호출되는 메소드입니다.
     * 사용자가 권한이 없거나 접근할 수 없는 리소스에 접근하려 할 때
     * 발생한 예외를 처리하고, 적절한 응답을 클라이언트에 보냅니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param accessDeniedException 접근 거부 예외 객체
     * @throws IOException      입출력 오류가 발생하면 예외를 던집니다.
     * @throws ServletException 서블릿 관련 예외가 발생하면 예외를 던집니다.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws
            IOException, ServletException {
        log.warn("Access Denied for request URL: {} - Reason: {}", request.getRequestURI(), accessDeniedException.getMessage());

        response.sendRedirect("/error");
    }
}
