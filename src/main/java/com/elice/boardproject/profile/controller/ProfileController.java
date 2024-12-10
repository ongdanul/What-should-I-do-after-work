package com.elice.boardproject.profile.controller;

import com.elice.boardproject.global.config.CookieUtils;
import com.elice.boardproject.profile.entity.Profile;
import com.elice.boardproject.profile.service.ProfileService;
import com.elice.boardproject.user.dto.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    // 생성자를 통한 의존성 주입
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    private String getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof CustomOAuth2User) {
            return ((CustomOAuth2User) principal).getUserId();
        } else {
            return principal.toString();
        }
    }

    // MY 회원 정보 조회
    @GetMapping()
    public String profile(Model model) {
        String userId = getCurrentUserId();
        Profile profile = profileService.getProfileByUserId(userId);
        model.addAttribute("profile", profile);
        return "profile/profile";
    }

    /**
     * MY 회원 정보 수정 페이지를 처리합니다.
     * 현재 로그인한 사용자의 프로필 정보를 가져와 모델에 추가합니다.
     * 사용자가 소셜 로그인인 경우 소셜 회원 수정 페이지로,
     * 일반 회원인 경우 일반 회원 수정 페이지로 리다이렉트합니다.
     *
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 회원 정보 수정 페이지 이름
     */
    @GetMapping("/edit")
    public String editProfile(Model model) {
        String userId = getCurrentUserId();
        Profile profile = profileService.getProfileByUserId(userId);
        model.addAttribute("profile", profile);

        boolean isSocialLogin = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof CustomOAuth2User);
        if (isSocialLogin) {
            return "profile/editSocialUser";
        }

        return "profile/editUser";
    }

    /**
     * MY 회원 정보 수정 (비밀번호, 비밀번호 확인 비교)을 처리합니다.
     * 사용자가 수정한 프로필 정보를 받아 프로필을 업데이트하고,
     * 로그아웃 후 다시 로그인 페이지로 리다이렉트합니다.
     *
     * @param profile 수정된 사용자 프로필 정보
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param redirectAttributes 리다이렉트 후 전달할 속성
     * @return 리다이렉트할 URL
     */
    @PostMapping("/edit")
    public String updateProfile(@Valid Profile profile, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            if (principal instanceof CustomOAuth2User) {
                profileService.updateSocialUserProfile(profile);
                CookieUtils.clearSocialLoginCookies(response);
                log.info("TEST - SocialUserProfile EDIT");
            } else if (principal instanceof UserDetails) {
                profileService.updateLocalUserProfile(profile);
                CookieUtils.clearLocalLoginCookies(response);
                log.info("TEST - LocalUserProfile EDIT");
            }
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

            redirectAttributes
                    .addFlashAttribute("message", "회원수정이 완료되었습니다. 다시 로그인해주세요.");

            return "redirect:/user/login";
        } catch (Exception  e) {
            return "redirect:/profile/edit";
        }
    }

    // MY 회원 탈퇴
    @PostMapping("/delete")
    public String deleteUser(HttpServletRequest request) {
        String userId = getCurrentUserId();

        // 사용자 데이터 삭제
        profileService.deleteProfileByUserId(userId);

        // 로그아웃 처리
        request.getSession().invalidate();
        return "redirect:/user/login";
    }
}



