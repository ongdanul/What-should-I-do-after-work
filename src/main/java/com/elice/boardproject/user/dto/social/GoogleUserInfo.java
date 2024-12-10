package com.elice.boardproject.user.dto.social;

import java.util.Map;

/**
 * 구글 로그인 사용자 정보를 처리하는 클래스입니다.
 * 구글 로그인 API로부터 받은 사용자 정보를 기반으로 각 필드를 추출합니다.
 */
public class GoogleUserInfo extends OAuth2UserInfo {

    /**
     * GoogleUserInfo 생성자
     * @param attributes 구글 로그인으로부터 받은 사용자 정보
     */
    public GoogleUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getId() {
        return attributes.get("sub") != null ? attributes.get("sub").toString() : null;
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
        return attributes.get("picture") != null ? attributes.get("picture").toString() : null;
    }
}
