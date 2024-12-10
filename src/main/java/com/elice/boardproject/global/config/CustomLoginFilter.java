package com.elice.boardproject.global.config;

import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.mapper.UsersMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.OAuth2RefreshTokenGrantRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final UsersMapper usersMapper;
    private final AuthenticationManager authenticationManager;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private static final int ID_COOKIE_EXPIRATION = 30 * 24 * 60 * 60; //30days

    /**
     * LoginFilter 생성자
     *
     * @param authenticationManager 인증 매니저
     * @param usersMapper 사용자 정보 조회를 위한 매퍼
     * @param customAuthenticationFailureHandler 인증 실패 처리기
     */
    public CustomLoginFilter(AuthenticationManager authenticationManager, UsersMapper usersMapper, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.authenticationManager = authenticationManager;
        this.usersMapper = usersMapper;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        setFilterProcessesUrl("/user/loginProcess");
    }

    /**
     * 사용자가 제출한 로그인 정보를 이용해 인증을 시도합니다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @return 인증된 사용자 정보
     * @throws AuthenticationException 인증 실패 시 예외 발생
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
            AuthenticationException {
        //TODO 로그인 기능 완성후 TEST 로그 삭제하기
        log.info("Test - LoginFilter is being called"); //Test

        String userName = obtainUsername(request);
        String password = obtainPassword(request);

        boolean rememberId = "true".equals(request.getParameter("rememberId"));
        boolean rememberMe = "true".equals(request.getParameter("rememberMe"));

        String rememberIdString = Boolean.toString(rememberId); //Test
        String rememberMeString = Boolean.toString(rememberMe); //Test

        log.info("Test - rememberId: {}, rememberMe: {}", rememberIdString, rememberMeString); //Test

        Map<String, Boolean> loginDetails = new HashMap<>();
        loginDetails.put("rememberId", rememberId);
        loginDetails.put("rememberMe", rememberMe);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userName, password, null);
        authToken.setDetails(loginDetails);

        return authenticationManager.authenticate(authToken);
    }

    /**
     * 로그인 성공 시 호출되는 메서드로, 성공적인 인증 후 추가 처리를 수행합니다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param chain 필터 체인
     * @param authentication 인증된 사용자 정보
     * @throws IOException 입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String userId = authentication.getName();

        Users user = usersMapper.findByUser(userId);

        if (isUserLoginLocked(response, user)) {
            return;
        }

        resetLoginAttempts(user);

        handleRememberIdCookie(response, authentication, userId);

//        final int ACCESSTOKEN_VALIDITY = 60 * 60;
//        // 소셜 로그인 자동 로그인 처리
//        String accessToken = CookieUtils.getCookieValue(request, "access_token");
//        if (!StringUtils.hasText(accessToken)) {
//            // 엑세스 토큰 쿠키가 없으면 리프레시 토큰 쿠키를 찾음
//            String refreshToken = CookieUtils.getCookieValue(request, "refresh_token");
//
//            if (!StringUtils.hasText(refreshToken)) {
//                // 리프레시 토큰도 없으면 로그인 다시 요청
//                log.error("Refresh token invalid. Please reauthorize.");
//                response.sendRedirect("/user/login");
//            }
//
//            // 리프레시 토큰으로 새 액세스 토큰 발급
//            try {
//                OAuth2AccessToken newAccessToken = refreshAccessToken(refreshToken ,userId);
//                CookieUtils.tokensInCookies(response, "access_token", newAccessToken.getTokenValue(), ACCESSTOKEN_VALIDITY);
//            } catch (OAuth2AuthenticationException e) {
//                log.error("Token refresh failed.: {}", e.getMessage());
//                response.sendRedirect("/user/login");
//            }
//        }

        chain.doFilter(request, response);
    }

    /**
     * 로그인 실패 시 호출되는 메서드로, 실패 시 커스텀 처리기를 이용해 실패 처리를 합니다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param failed 인증 실패 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        customAuthenticationFailureHandler.onAuthenticationFailure(request, response, failed);
    }

    /**
     * 사용자의 계정이 잠겨 있는지 확인하고, 잠긴 상태라면 응답으로 "LOGIN_LOCKED" 메시지를 반환합니다.
     *
     * @param response HTTP 응답 객체
     * @param user 사용자 정보
     * @return 로그인 잠금 상태라면 true, 아니면 false
     * @throws IOException 입출력 예외
     */
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

    /**
     * 로그인 시도 횟수를 리셋하고, 마지막 실패 로그인 시간을 null로 설정합니다.
     *
     * @param user 사용자 정보
     */
    private void resetLoginAttempts(Users user) {
        if (user != null && user.getLoginAttempts() > 0) {
            user.setLoginAttempts(0);
            user.setLastFailedLogin(null);
            try {
                usersMapper.editLoginLock(user);
            } catch (Exception e) {
                log.error("Error occurred while resetting login attempts for user: {}", user.getUserId(), e);
            }
        }
    }

    /**
     * "rememberId" 쿠키를 생성하여 응답에 추가합니다.
     * "rememberId" 값이 true일 경우 사용자 ID를 Base64로 인코딩하여 쿠키에 저장합니다.
     *
     * @param response HTTP 응답 객체
     * @param authentication 인증된 사용자 정보
     * @param userId 사용자 ID
     */
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

//    private OAuth2AccessToken refreshAccessToken(String refreshToken, String userId) {
//        // 특정 사용자의 Authorized Client를 불러옴
//        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("clientRegistrationId", userId);
//
//        // 토큰 갱신 요청
//        if (authorizedClient != null && authorizedClient.getRefreshToken() != null) {
//            OAuth2AccessTokenResponse tokenResponse = tokenResponseClient.getTokenResponse(
//                    new OAuth2RefreshTokenGrantRequest(
//                            authorizedClient.getClientRegistration(),
//                            authorizedClient.getRefreshToken()
//                    )
//            );
//            return tokenResponse.getAccessToken();
//        }
//
//        throw new OAuth2AuthenticationException(new OAuth2Error("invalid_grant", "Unable to refresh token.", null));
//    }
}
