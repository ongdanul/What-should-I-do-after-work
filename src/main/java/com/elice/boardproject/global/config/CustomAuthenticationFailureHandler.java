package com.elice.boardproject.global.config;

import com.elice.boardproject.users.entity.Users;
import com.elice.boardproject.users.mapper.UsersMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final UsersMapper usersMapper;

    public CustomAuthenticationFailureHandler(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    private static final int MAX_ATTEMPTS = 5;
    private final ConcurrentHashMap<String, Integer> loginAttempts = new ConcurrentHashMap<>();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String userId = request.getParameter("username");

        Users user = usersMapper.findByUserId(userId);

        int attempts = loginAttempts.getOrDefault(userId, 0) + 1;

        //유저가 존재하는 경우에만 실패 횟수 추가
        if(user != null) {
            loginAttempts.put(userId, attempts);
        }

        if (attempts >= MAX_ATTEMPTS) {
            if (user != null) {
                user.setLoginLock(true);
                usersMapper.loginLock(user);
                loginAttempts.remove(userId); // 잠금 처리 후 실패 횟수 초기화

                response.setContentType("text/plain;charset=UTF-8");
                response.setStatus(HttpStatus.FORBIDDEN.value()); //403
                response.getWriter().write("LOGIN_LOCKED");
                return;
            }
        }

        // 이미 잠금된 계정인 경우
        if (user != null && user.isLoginLock()) {
            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("LOGIN_LOCKED");
            return;
        }

        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); //401
        response.getWriter().write("LOGIN_FAILED");

    }
}
