package com.elice.boardproject.user.mapper;

import com.elice.boardproject.user.entity.SocialUsers;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SocialUsersMapper {
    /**
     * 새 사용자 계정을 등록합니다. (소셜 회원가입)
     * @param socialUser 사용자 정보를 포함한 SocialUsers 객체
     */
    void registerSocialUser(SocialUsers socialUser);

    /**
     * 주어진 provider와 providerId로 사용자 정보를 조회합니다.
     * @param provider 소셜 로그인 제공자
     * @param providerId 소셜 고유 ID
     * @return 해당하는 SocialUsers 객체
     */
    SocialUsers findByProviderAndProviderId(String provider, String providerId);
}
