package com.elice.boardproject.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthorizedClientService authorizedClientService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final int ACCESSTOKEN_VALIDITY = 60 * 60;  //1hours
        final int REFRESHTOKEN_VALIDITY = 7 * 24 * 60 * 60;  //7days

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getName());
        log.info("Test - CustomOauth2SuccessHandler token: {}", authentication);
        log.info("Test - CustomOauth2SuccessHandler authorizedClient: {}, {}", token.getAuthorizedClientRegistrationId(), token.getName());
        log.info("Test - CustomOauth2SuccessHandler authorizedClient: {}", authorizedClient.getClientRegistration());

        // 엑세스 토큰을 쿠키에 저장
        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        String refreshToken = authorizedClient.getRefreshToken() != null ? authorizedClient.getRefreshToken().getTokenValue() : null;
        log.info("Test - CustomOauth2SuccessHandler accessToken: {}", accessToken);
        log.info("Test - CustomOauth2SuccessHandler refreshToken: {}", refreshToken);
        CookieUtils.tokensInCookies(response, "access_token", accessToken, ACCESSTOKEN_VALIDITY);
        CookieUtils.tokensInCookies(response, "refresh_token", refreshToken, REFRESHTOKEN_VALIDITY);

        response.sendRedirect("/board/list");
    }
}
