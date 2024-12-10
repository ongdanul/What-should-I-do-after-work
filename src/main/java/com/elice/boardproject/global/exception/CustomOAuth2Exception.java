package com.elice.boardproject.global.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * OAuth2 인증 과정에서 발생하는 커스텀 예외를 정의하는 클래스입니다.
 * 이 예외는 인증 실패 시 발생하며, AuthenticationException을 확장합니다.
 */
public class CustomOAuth2Exception extends AuthenticationException {

    /**
     * CustomOAuth2Exception 생성자입니다.
     * 주어진 메시지를 사용하여 인증 예외를 생성합니다.
     *
     * @param message 예외 메시지
     */
    public CustomOAuth2Exception(String message) {
        super(message);
    }

}
