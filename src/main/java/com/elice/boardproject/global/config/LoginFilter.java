package com.elice.boardproject.global.config;

import com.elice.boardproject.users.entity.Users;
import com.elice.boardproject.usersAuth.entity.UsersAuth;
import com.elice.boardproject.users.mapper.UsersMapper;
import com.elice.boardproject.usersAuth.mapper.UsersAuthMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final UsersMapper usersMapper;
    private final UsersAuthMapper usersAuthMapper;
    private final AuthenticationManager authenticationManager;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    public LoginFilter(AuthenticationManager authenticationManager, UsersAuthMapper usersAuthMapper, UsersMapper usersMapper,
                       CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.authenticationManager = authenticationManager;
        this.usersMapper = usersMapper;
        this.usersAuthMapper = usersAuthMapper;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
            AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        boolean rememberMe = "true".equals(request.getParameter("rememberMe"));
        boolean rememberId = "true".equals(request.getParameter("rememberId"));

        Map<String, Boolean> loginDetails = new HashMap<>();
        loginDetails.put("rememberMe", rememberMe);
        loginDetails.put("rememberId", rememberId);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        authToken.setDetails(loginDetails);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) {

        @SuppressWarnings("unchecked")
        Map<String, Boolean> loginDetails = (Map<String, Boolean>) authentication.getDetails();

        Boolean rememberId = loginDetails.get("rememberId");
        String userId = authentication.getName();
//        String role = getAuthoritiesByUserId(userId);

        Users user = usersMapper.findByUserId(userId);

        if (user != null && user.isLoginLock()) {
            try {
                response.setContentType("text/plain;charset=UTF-8");
                response.setStatus(HttpStatus.FORBIDDEN.value()); // 403 Forbidden 상태 코드 반환
                response.getWriter().write("LOGIN_LOCKED");
            } catch (IOException e) {
                log.error("IOException occurred while redirecting", e);
            }
            return;
        }

        if (rememberId) {
            Cookie cookie = new Cookie("rememberId", userId);
            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookie.setPath("/");
//            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("rememberId", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
//            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        customAuthenticationFailureHandler.onAuthenticationFailure(request, response, failed);
    }

    public String getAuthoritiesByUserId(String userId) {

        UsersAuth usersAuth = usersAuthMapper.findByUserId(userId);

        if (usersAuth != null) {
            return usersAuth.getAuthorities();
        }

        return null;
    }

}
