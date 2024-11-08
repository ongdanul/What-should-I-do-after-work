package com.elice.boardproject.profile.controller;

import com.elice.boardproject.profile.entity.Profile;
import com.elice.boardproject.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping()
    public String profile(Model model) {
        // UserId가 'gogo'인 경우 조회
        String userId = "gogo";
        Profile profile = profileService.getProfileByUserId(userId);
        model.addAttribute("profile", profile);
        return "profile/profile";
    }

    @GetMapping("/edit")
    public String editProfile(Model model) {
        // UserId가 'gogo'인 경우 조회
        String userId = "gogo";
        Profile profile = profileService.getProfileByUserId(userId);
        model.addAttribute("profile", profile);
        return "profile/edit";
    }

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
}


