package com.elice.boardproject.user.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2 사용자 인증 정보를 처리하는 CustomOAuth2User 클래스입니다.
 * Spring Security의 OAuth2User 인터페이스를 구현하여 OAuth2 인증 정보를 사용자 정의 방식으로 처리합니다.
 */
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final Oauth2DTO oauth2DTO;

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", oauth2DTO.getUserId());
        attributes.put("userName", oauth2DTO.getUserName());
        attributes.put("authorities", oauth2DTO.getAuthorities());
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add((GrantedAuthority) oauth2DTO::getAuthorities);

        return collection;
    }

    @Override
    public String getName() {
        return oauth2DTO.getUserName();
    }

    public String getUserId() {
        return oauth2DTO.getUserId();
    }
}