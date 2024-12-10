package com.elice.boardproject.user.dto.social;

import java.util.Map;

/**
 * 네이버 사용자 정보를 처리하는 클래스입니다.
 * OAuth2 인증을 통해 제공된 네이버 사용자 정보를 파싱하여
 * 각 정보를 반환하는 메소드들을 제공합니다.
 */
public class NaverUserInfo extends OAuth2UserInfo {

    /**
     * NaverUserInfo 생성자입니다.
     * 네이버에서 제공된 사용자 정보를 응답(response)으로부터 추출하여 부모 클래스에 전달합니다.
     * @param attributes 네이버 OAuth2 인증에서 제공한 사용자 정보
     */
    public NaverUserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get("response"));
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getId() {
        return attributes.get("id") != null ? attributes.get("id").toString() : null;
    }

    @Override
    public String getEmail() {
        return attributes.get("email") != null ? attributes.get("email").toString() : null;
    }

    @Override
    public String getName() {
        return attributes.get("name") != null ? attributes.get("name").toString() : null;
    }

    @Override
    public String getProfileUrl() {
        return attributes.get("profile_image") != null ? attributes.get("profile_image").toString() : null;
    }
}
