package com.elice.boardproject.admin.controller;

import com.elice.boardproject.admin.entity.Admin;
import com.elice.boardproject.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
/*
@Controller
@RequestMapping("/admin")
public class AdminProfileController{

    @GetMapping()
    public String admin(Model model) {
        return "profile/admin";
    }
}
*/

/*@GetMapping("/admin")
    public String admin(Model model) {
        List<AdminProfile> users = profileService.getAllProfiles();
        model.addAttribute("users", users);
        return "profile/admin";
    }*/

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping()
    public String admin(Model model) {
        List<Admin> users = adminService.getAllProfiles();
        model.addAttribute("users", users);
        return "admin/admin";
    }

    /*@PostMapping("/delete")
    public String deleteProfile(@RequestParam("userId") String userId) {
        profileService.deleteProfileByUserId(userId);
        return "redirect:/profile/admin";
    }
*/
    /*@PostMapping("/toggleLock")
    public void toggleLoginLock(@RequestParam("userId") String userId, @RequestParam("loginLock") boolean loginLock) {
        profileService.toggleLoginLock(userId, loginLock);
    }*/
}