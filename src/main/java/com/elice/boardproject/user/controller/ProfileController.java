package com.elice.boardproject.user.controller;

import com.elice.boardproject.user.entity.Profile;
import com.elice.boardproject.user.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/{userId}")
    public String getProfileByUserId(@PathVariable String userId, Model model) {
        Profile profile = profileService.getProfileByUserId(userId);
        model.addAttribute("profile", profile);
        return "user/profile";
    }

    @PutMapping("/{userId}")
    @ResponseBody
    public String updateProfile(@PathVariable String userId, @RequestBody Profile profile) {
        profile.setUserId(userId);
        profileService.updateProfile(profile);
        return "Profile updated successfully";
    }
}
