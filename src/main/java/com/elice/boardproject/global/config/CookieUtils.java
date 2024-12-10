package com.elice.boardproject.global.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {

    /**
     * 소셜 로그인 쿠키를 삭제하는 메소드입니다.
     *
     * @param response HTTP 응답 객체
     */
    public static void clearSocialLoginCookies(HttpServletResponse response) {
        // 소셜 로그인 관련 쿠키 삭제
        Cookie accessTokenCookie = deleteCookie("access_token");
        Cookie refreshTokenCookie = deleteCookie("refresh_token");

        // 쿠키를 응답에 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    /**
     * 로컬 로그인 쿠키를 삭제하는 메소드입니다.
     *
     * @param response HTTP 응답 객체
     */
    public static void clearLocalLoginCookies(HttpServletResponse response) {
        // 로컬 로그인 관련 쿠키 삭제
        Cookie sessionCookie = deleteCookie("JSESSIONID");
        Cookie rememberMeCookie = deleteCookie("remember-me");

        // 쿠키를 응답에 추가
        response.addCookie(sessionCookie);
        response.addCookie(rememberMeCookie);
    }

    /**
     * 쿠키를 삭제하는 메소드
     * 쿠키를 삭제하기 위해 해당 쿠키의 값을 null로 설정하고,
     * 만료 시간을 0으로 설정하여 클라이언트에게 해당 쿠키를 삭제하도록 요청합니다.
     * @param cookieName 삭제할 쿠키 이름
     * @return 설정된 쿠키
     */
    private static Cookie deleteCookie(String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * 클라이언트 요청에서 특정 쿠키의 값을 조회하는 메소드.
     * 해당 이름의 쿠키가 존재하면 그 값을 반환하고, 없으면 null을 반환합니다.
     *
     * @param request 클라이언트 요청 객체
     * @param name 조회할 쿠키의 이름
     * @return 지정된 쿠키의 값, 쿠키가 없으면 null
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 쿠키를 생성하고 응답 객체에 추가하는 메서드
     *
     * @param response HTTP 응답 객체
     * @param cookieName 쿠키 이름
     * @param value 쿠키 값
     * @param maxAge 쿠키 만료 시간 (초 단위)
     */
    public static void tokensInCookies(HttpServletResponse response, String cookieName, String value, int maxAge) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}
