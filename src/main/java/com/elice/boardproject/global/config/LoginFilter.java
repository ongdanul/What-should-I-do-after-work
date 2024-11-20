package com.elice.boardproject.global.config;

import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.mapper.UsersMapper;
import jakarta.servlet.FilterChain;
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
    private final AuthenticationManager authenticationManager;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    public LoginFilter(AuthenticationManager authenticationManager, UsersMapper usersMapper,
                       CustomAuthenticationFailureHandler customAuthenticationFailureHandler/*, TokenBasedRememberMeServices rememberMeServices*/) {
        this.authenticationManager = authenticationManager;
        this.usersMapper = usersMapper;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
            AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        boolean rememberMe = "true".equals(request.getParameter("rememberMe"));

        Map<String, Boolean> loginDetails = new HashMap<>();
        loginDetails.put("rememberMe", rememberMe);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        authToken.setDetails(loginDetails);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) {

        String userId = authentication.getName();

        Users user = usersMapper.findByUser(userId);

        if (user != null && user.isLoginLock()) {
            try {
                response.setContentType("text/plain;charset=UTF-8");
                response.setStatus(HttpStatus.FORBIDDEN.value()); // 403 Forbidden 상태 코드 반환
                response.getWriter().write("LOGIN_LOCKED");
            } catch (IOException e) {
                log.error("IOException occurred while redirecting", e);
            }
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        customAuthenticationFailureHandler.onAuthenticationFailure(request, response, failed);
    }
}
