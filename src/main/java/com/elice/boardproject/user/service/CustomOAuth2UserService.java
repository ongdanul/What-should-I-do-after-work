package com.elice.boardproject.user.service;

import com.elice.boardproject.global.exception.CustomOAuth2Exception;
import com.elice.boardproject.user.dto.CustomOAuth2User;
import com.elice.boardproject.user.dto.Oauth2DTO;
import com.elice.boardproject.user.dto.social.GoogleUserInfo;
import com.elice.boardproject.user.dto.social.KakaoUserInfo;
import com.elice.boardproject.user.dto.social.NaverUserInfo;
import com.elice.boardproject.user.dto.social.OAuth2UserInfo;
import com.elice.boardproject.user.entity.SocialUsers;
import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.entity.UsersAuth;
import com.elice.boardproject.user.mapper.SocialUsersMapper;
import com.elice.boardproject.user.mapper.UsersAuthMapper;
import com.elice.boardproject.user.mapper.UsersMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * 커스텀 OAuth2 사용자 서비스 클래스입니다.
 * 다양한 소셜 로그인(카카오, 네이버, 구글)을 처리하고, 기존 사용자 또는 새로운 사용자를 등록하여 반환합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsersMapper usersMapper;
    private final UsersAuthMapper usersAuthMapper;
    private final SocialUsersMapper socialUsersMapper;
    private static final String DEFAULT_PROFILE_URL = "/img/defaultProfile.png";

    /**
     * OAuth2 사용자 정보를 로드합니다.
     * 제공된 OAuth2 사용자 정보를 기반으로 소셜 로그인 처리 및 사용자 등록을 담당합니다.
     * @param userRequest OAuth2 사용자 요청 정보
     * @return OAuth2User 소셜 로그인 사용자 정보
     * @throws OAuth2AuthenticationException 인증 오류가 발생한 경우
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("Test - CustomOAuth2UserService OAuth2User attributes: {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo;

        switch (provider) {
            case "kakao":
                oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
                break;
            case "naver":
                oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
                break;
            case "google":
                oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
                break;
            default:
                throw new OAuth2AuthenticationException("Unsupported Provider");
        }

        return oAuth2UserProcess(oAuth2UserInfo);
    }

    /**
     * 소셜 사용자 정보 처리 및 등록을 담당하는 메서드입니다.
     * @param oAuth2UserInfo 소셜 로그인 사용자 정보
     * @return OAuth2User 처리된 소셜 로그인 사용자 정보
     * @throws CustomOAuth2Exception 사용자 처리 중 예외 발생 시
     */
    private OAuth2User oAuth2UserProcess(OAuth2UserInfo oAuth2UserInfo) {
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getId();
        String userId = provider + "_" + providerId;

        Users existUser = usersMapper.findByUser(userId);
        if (existUser != null) {
            if (!existUser.isSocial()) {
                log.warn("The ID is already registered as a local user");
                throw new CustomOAuth2Exception("해당 ID로 이미 일반 회원가입이 되어 있습니다.\\n일반 로그인을 시도해 주세요.");
            }
            return loadExistingSocialUser(existUser, oAuth2UserInfo);
        }

        SocialUsers socialUser = socialUsersMapper.findByProviderAndProviderId(provider, providerId);
        if (socialUser != null) {
            log.warn("Social user already exists: {}", socialUser.getUserId());
            throw new CustomOAuth2Exception("해당 소셜 계정으로 이미 가입되어 있습니다.");
        }

        return registerNewUser(oAuth2UserInfo);
    }

    /**
     * 기존 소셜 로그인 사용자의 정보를 로드하여 반환합니다.
     * @param existUser 기존 사용자 정보
     * @param oAuth2UserInfo 소셜 로그인 사용자 정보
     * @return OAuth2User 기존 소셜 사용자 정보
     */
    private OAuth2User loadExistingSocialUser(Users existUser, OAuth2UserInfo oAuth2UserInfo) {
        UsersAuth usersAuth = usersAuthMapper.findByUserId(existUser.getUserId());

        Oauth2DTO oauth2DTO = Oauth2DTO.builder()
                .userId(existUser.getUserId())
                .userName(oAuth2UserInfo.getName())
                .authorities(usersAuth.getAuthorities())
                .build();

        return new CustomOAuth2User(oauth2DTO);
    }

    /**
     * 새로운 소셜 로그인 사용자 정보를 등록합니다.
     * @param oAuth2UserInfo 소셜 로그인 사용자 정보
     * @return OAuth2User 등록된 새로운 사용자 정보
     */
    private OAuth2User registerNewUser(OAuth2UserInfo oAuth2UserInfo) {
        String DEFAULT_ROLE = "ROLE_USER";

        Users user = new Users();
        user.setUserId(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getId());
        user.setUserPassword("-");
        user.setUserName(oAuth2UserInfo.getName());
        user.setContact("-");
        if(oAuth2UserInfo.getEmail() == null || oAuth2UserInfo.getEmail().isEmpty()){
            user.setEmail("-");
        }
        user.setEmail(oAuth2UserInfo.getEmail());
        if(oAuth2UserInfo.getProfileUrl() == null || oAuth2UserInfo.getProfileUrl().isEmpty()){
            user.setProfileUrl(DEFAULT_PROFILE_URL);
        }
        user.setProfileUrl(oAuth2UserInfo.getProfileUrl());
        user.setSocial(true);

        usersMapper.registerOauthUser(user);

        SocialUsers socialUser = new SocialUsers();
        socialUser.setUserId(user.getUserId());
        socialUser.setProvider(oAuth2UserInfo.getProvider());
        socialUser.setProviderId(oAuth2UserInfo.getId());

        socialUsersMapper.registerSocialUser(socialUser);

        usersAuthMapper.registerUserAuth(user.getUserId());

        Oauth2DTO oauth2DTO = Oauth2DTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .authorities(DEFAULT_ROLE)
                .build();

        return new CustomOAuth2User(oauth2DTO);
    }

}
