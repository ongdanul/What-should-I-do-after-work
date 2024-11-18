package com.elice.boardproject.admin.controller;

import com.elice.boardproject.admin.entity.Admin;
import com.elice.boardproject.admin.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    public AdminService adminService;

    // 생성자를 통한 의존성 주입
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 전체 회원 조회
    @GetMapping()
    public String admin(Model model) {
        List<Admin> users = adminService.getAllUsersWithAuth();
        model.addAttribute("users", users);

        // 전체 사용자수, 관리자수 조회
        int totalUsers = (int) users.stream()
                .filter(user -> "ROLE_USER".equals(user.getAuthorities()))
                .count();
        long totalAdmins = users.stream()
                .filter(user -> "ROLE_ADMIN".equals(user.getAuthorities()))
                .count();
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalAdmins", totalAdmins);

        return "admin/admin";
    }

    // 회원 삭제
    @PostMapping("/delete")
    @ResponseBody
    public String deleteUser(@RequestParam("userId") String userId) {
        adminService.deleteProfileByUserId(userId);

        return "success";
    }

    // 회원 선택 삭제
    @PostMapping("/deleteSelected")
    @ResponseBody
    public String deleteSelected(@RequestBody Map<String, List<String>> request) {
        List<String> userIds = request.get("userIds");
        adminService.deleteProfilesByUserIds(userIds);

        return "success";
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

    // 로그인 (잠금/해제)
    @PostMapping("/toggleLoginLock")
    @ResponseBody
    public String toggleLoginLock(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        boolean newLockStatus = Boolean.parseBoolean(request.get("newLockStatus"));
        adminService.toggleLoginLock(userId, newLockStatus);

        return "success";
    }

    // 필터 및 검색 적용된 회원 조회
    @GetMapping("/filtered")
    public String filteredAdmin(
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        String role = null;
        Boolean loginLock = null;

        // filter 값에 따라 role과 loginLock 설정
        if (filter != null) {
            switch (filter) {
                case "ROLE_ADMIN", "ROLE_USER" -> role = filter;  // ROLE_ADMIN 또는 ROLE_USER로 설정
                case "LOCKED_TRUE" -> loginLock = true;  // 잠김
                case "LOCKED_FALSE" -> loginLock = false;  // 해제
            }
        }

        List<Admin> users = adminService.getFilteredUsers(role, loginLock, keyword);
        model.addAttribute("users", users);

        // 필터 호출 시, 전체 사용자수, 관리자수 조회
        List<Admin> countUsers = adminService.getAllUsersWithAuth();

        int totalUsers = (int) countUsers.stream()
                .filter(user -> "ROLE_USER".equals(user.getAuthorities()))
                .count();
        long totalAdmins = countUsers.stream()
                .filter(user -> "ROLE_ADMIN".equals(user.getAuthorities()))
                .count();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalAdmins", totalAdmins);

        return "admin/admin";
    }
}