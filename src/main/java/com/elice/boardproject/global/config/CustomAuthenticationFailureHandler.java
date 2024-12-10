package com.elice.boardproject.global.config;

import com.elice.boardproject.global.exception.CustomOAuth2Exception;
import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.mapper.UsersMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final UsersMapper usersMapper;

    /**
     * 로그인 인증 실패 시 호출되는 메소드로, 로그인 실패에 대한 처리를 수행합니다.
     * OAuth2 인증 실패나 일반 로그인 실패에 대해 각각 다른 처리를 합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param exception 인증 실패 시 발생한 예외
     * @throws IOException 입출력 오류가 발생하면 예외를 던집니다.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        // OAuth2 인증 실패인 경우, 커스텀 오류 메시지를 포함한 HTML로 응답
        if (exception instanceof CustomOAuth2Exception) {
            response.setContentType("text/html;charset=UTF-8");
            String errorMessage = exception.getMessage();
            response.getWriter().write(
                    "<html><script>"
                            + "alert('" + errorMessage + "');"
                            + "window.location.href = '/user/login';"
                            + "</script></html>"
            );
            return;
        }

        // 일반 로그인 실패 처리
        String userId = request.getParameter("username");

        log.info("Test - onAuthenticationFailure userId : {}", userId);

        // 로그인 실패 처리: 실패 횟수 증가 및 잠금 여부 처리
        boolean isLocked = handleLoginFailure(userId);

        // 이미 잠금된 계정인 경우
        if (isLocked) {
            sendErrorResponse(response, HttpStatus.FORBIDDEN.value(), "LOGIN_LOCKED");
            return;
        }
        sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "LOGIN_FAILED");
    }

    /**
     * 로그인 실패 시 클라이언트에 에러 메시지를 전송하는 메소드입니다.
     *
     * @param response  HTTP 응답 객체
     * @param statusCode 응답 상태 코드
     * @param message 에러 메시지
     * @throws IOException 입출력 오류가 발생하면 예외를 던집니다.
     */
    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write(message);
    }

    /**
     * 로그인 실패 시, 실패 횟수를 증가시키고 계정 잠금 여부를 처리합니다.
     * - 실패 횟수가 설정된 최대 횟수에 도달하면 계정을 잠금 처리합니다.
     * - 실패 횟수는 마지막 로그인 실패 시간에 따라 초기화될 수 있습니다.
     *
     * @param userId 사용자 ID
     * @return 계정이 잠금 상태인 경우 true, 그렇지 않으면 false
     */
    public boolean handleLoginFailure(String userId) {
        final int MAX_ATTEMPTS = 5;

        Users user = usersMapper.findByUser(userId);

        if (user == null) {
            log.error("User not found: {}", userId);
            return false;
        }

        // 계정이 이미 잠금 상태인 경우
        if (user.isLoginLock()) {
            log.error("Locked account: {} ", userId);
            return true;
        }

        // 현재 실패 횟수로 계정 잠금 여부 검증 (MAX_ATTEMPTS 이상이면 잠금)
        if (user.getLoginAttempts() >= MAX_ATTEMPTS) {
            user.setLoginLock(true);
            updateUserState(user);
            return true;
        }

        // 실패 횟수 증가
        int attempts = user.getLoginAttempts() + 1;
        user.setLoginAttempts(attempts); // 로그인 실패 횟수 저장
        user.setLastFailedLogin(Instant.now()); // 마지막 실패 시간 갱신

        // 실패 횟수가 MAX_ATTEMPTS에 도달하면 잠금 처리
        if (attempts >= MAX_ATTEMPTS) {
            user.setLoginLock(true);
            updateUserState(user);
            return true;
        }

        updateUserState(user);
        return false;
    }

    /**
     * 사용자 상태를 데이터베이스에 업데이트하는 메소드입니다.
     * 로그인 실패 횟수 및 잠금 상태 등의 정보를 업데이트합니다.
     *
     * @param user 업데이트할 사용자 객체
     */
    private void updateUserState(Users user) {
        try {
            usersMapper.editLoginLock(user);
        } catch (Exception e) {
            log.error("Error occurred while updating user login status: {}", user.getUserId(), e);
            throw new RuntimeException("An error occurred while updating user status.");
        }
    }
}
