package com.elice.boardproject.user.dto.social;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * OAuth2 인증을 통해 얻은 사용자 정보를 추상화하는 클래스입니다.
 * 이 클래스를 상속받아 각 소셜 로그인 제공자에 맞는 사용자 정보 처리 클래스를 구현해야 합니다.
 */
@RequiredArgsConstructor
public abstract class OAuth2UserInfo {
    /**
     * 소셜 로그인 제공자로부터 받은 사용자 정보 맵입니다.
     * 각 제공자마다 사용자 정보 구조가 다르므로 이를 처리하기 위해 사용됩니다.
     */
    protected final Map<String, Object> attributes;
    public abstract String getProvider();

    public abstract String getId();

    public abstract String getEmail();

    public abstract String getName();

    public abstract String getProfileUrl();
}
