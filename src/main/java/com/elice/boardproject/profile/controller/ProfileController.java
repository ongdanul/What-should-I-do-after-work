package com.elice.boardproject.profile.controller;

import com.elice.boardproject.profile.entity.Profile;
import com.elice.boardproject.profile.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    // 생성자를 통한 의존성 주입
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // MY 회원 정보 조회
    @GetMapping()
    public String profile(Model model) {
        // 현재 로그인된 사용자 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        Profile profile = profileService.getProfileByUserId(userId);
        model.addAttribute("profile", profile);
        return "profile/profile";
    }

    // MY 회원 정보 수정
    @GetMapping("/edit")
    public String editProfile(Model model) {
        // 현재 로그인된 사용자 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        Profile profile = profileService.getProfileByUserId(userId);
        model.addAttribute("profile", profile);
        return "profile/edit";
    }

    // MY 회원 정보 수정 (비밀번호, 비밀번호 확인 비교)
    @PostMapping("/edit")
    public String updateProfile(Profile profile, String confirmPw, Model model) {
        // 비밀번호와 비밀번호 확인 비교
        if (!profile.getUserPw().equals(confirmPw)) {
            model.addAttribute("errorMessage", "비밀번호 확인이 일치하지 않습니다.");
            return "profile/edit";
        }
        profileService.updateProfile(profile);
        return "redirect:/profile";
    }

    // MY 회원 탈퇴
    @PostMapping("/delete")
    public String deleteUser(HttpServletRequest request) {
        // 현재 로그인된 사용자 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        // 사용자 데이터 삭제
        profileService.deleteProfileByUserId(userId);

        // 로그아웃 처리
        request.getSession().invalidate();
        return "redirect:/user/login";
    }
}



