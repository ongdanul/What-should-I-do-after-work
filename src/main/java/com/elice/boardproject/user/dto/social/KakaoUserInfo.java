package com.elice.boardproject.user.dto.social;

import java.util.Map;

/**
 * 카카오 로그인 사용자 정보를 처리하는 클래스입니다.
 * 카카오 로그인 API로부터 받은 사용자 정보를 기반으로 각 필드를 추출합니다.
 */
public class KakaoUserInfo extends OAuth2UserInfo {

    /**
     * KakaoUserInfo 생성자
     * @param attributes 카카오 로그인으로부터 받은 사용자 정보
     */
    public KakaoUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getId() {
        return attributes.get("id") != null ? attributes.get("id").toString() : null;
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        return kakaoAccount != null && kakaoAccount.get("email") != null ? kakaoAccount.get("email").toString() : null;
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>)attributes.get("properties");
        return properties != null && properties.get("nickname") != null ? properties.get("nickname").toString() : null;
    }

    @Override
    public String getProfileUrl() {
        Map<String, Object> properties = (Map<String, Object>)attributes.get("properties");
        return properties != null && properties.get("profile_image") != null ? properties.get("profile_image").toString() : null;
    }
}
