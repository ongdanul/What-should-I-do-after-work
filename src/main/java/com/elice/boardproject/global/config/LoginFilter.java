package com.elice.boardproject.global.config;

import com.elice.boardproject.user.dto.CustomUserDetails;
import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.entity.UsersAuth;
import com.elice.boardproject.user.mapper.UsersAuthMapper;
import com.elice.boardproject.user.mapper.UsersMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final UsersMapper usersMapper;
    private final AuthenticationManager authenticationManager;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private static final int ID_COOKIE_EXPIRATION = 30 * 24 * 60 * 60; //30days
    public LoginFilter(AuthenticationManager authenticationManager, UsersMapper usersMapper, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.authenticationManager = authenticationManager;
        this.usersMapper = usersMapper;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        setFilterProcessesUrl("/user/loginProcess");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
            AuthenticationException {
        //TODO 로그인 기능 완성후 TEST 로그 삭제하기
        log.info("Test - LoginFilter is being called"); //Test

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        boolean rememberId = "true".equals(request.getParameter("rememberId"));
        boolean rememberMe = "true".equals(request.getParameter("rememberMe"));

        String rememberIdString = Boolean.toString(rememberId); //Test
        String rememberMeString = Boolean.toString(rememberMe); //Test

        log.info("Test - rememberId: {}, rememberMe: {}", rememberIdString, rememberMeString); //Test

        Map<String, Boolean> loginDetails = new HashMap<>();
        loginDetails.put("rememberId", rememberId);
        loginDetails.put("rememberMe", rememberMe);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        authToken.setDetails(loginDetails);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String userId = authentication.getName();

        Users user = usersMapper.findByUser(userId);

        if (isUserLoginLocked(response, user)) {
            return;
        }

        handleRememberIdCookie(response, authentication, userId);

        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        customAuthenticationFailureHandler.onAuthenticationFailure(request, response, failed);
    }

    private boolean isUserLoginLocked(HttpServletResponse response, Users user) throws IOException {
        if (user != null && user.isLoginLock()) {
            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("LOGIN_LOCKED");
            log.info("User account is locked: {}", user.getUserId());
            return true;
        }
        return false;
    }

    private void handleRememberIdCookie(HttpServletResponse response, Authentication authentication, String userId) {
        @SuppressWarnings("unchecked")
        Map<String, Boolean> loginDetails = (Map<String, Boolean>) authentication.getDetails();
        Boolean rememberId = loginDetails.getOrDefault("rememberId", false);

        Cookie rememberIdCookie;
        if (Boolean.TRUE.equals(rememberId)) {
            String encodedUserId = Base64.getEncoder().encodeToString(userId.getBytes(StandardCharsets.UTF_8));
            rememberIdCookie = new Cookie("remember-id", encodedUserId);
            rememberIdCookie.setMaxAge(ID_COOKIE_EXPIRATION);
        } else {
            rememberIdCookie = new Cookie("remember-id", null);
            rememberIdCookie.setMaxAge(0); // Remove cookie
        }
        rememberIdCookie.setPath("/");
        response.addCookie(rememberIdCookie);

        log.info("Remember ID cookie processed: {}", rememberId); //TODO debug로 수정하기
    }
}
