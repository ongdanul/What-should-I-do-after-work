package com.elice.boardproject.admin.controller;

import com.elice.boardproject.admin.entity.Admin;
import com.elice.boardproject.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 전체 회원 조회
    @GetMapping()
    public String admin(Model model) {
        List<Admin> users = adminService.getAllUsersWithAuth();
        model.addAttribute("users", users);
        return "admin/admin";
    }

    // 회원 삭제
    @PostMapping("/delete")
    public String deleteProfile(@RequestParam("userId") String userId) {
        adminService.deleteProfileByUserId(userId);
        return "redirect:/admin";
    }

    // 관리자 권한 (부여/회수)
    @PostMapping("/toggleAdmin")
    @ResponseBody
    public String toggleAdmin(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String newRole = request.get("newRole");
        adminService.toggleAdmin(userId, newRole);
        return "success";
    }

    // 로그인 (잠금/잠금 해제)
    @PostMapping("/toggleLoginLock")
    @ResponseBody
    public String toggleLoginLock(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        boolean newLockStatus = Boolean.parseBoolean(request.get("newLockStatus"));
        adminService.toggleLoginLock(userId, newLockStatus);
        return "success";
    }
}