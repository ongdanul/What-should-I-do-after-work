package com.elice.boardproject.admin.service;

import com.elice.boardproject.admin.entity.Admin;
import com.elice.boardproject.admin.mapper.AdminMapper;
import com.elice.boardproject.user.entity.UsersAuth;
import com.elice.boardproject.user.mapper.UsersAuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    @Autowired
    private final AdminMapper adminMapper;
    @Autowired
    private final UsersAuthMapper usersAuthMapper;

    // 전체 회원 조회
    public List<Admin> getAllUsersWithAuth() {
        List<Admin> admins = adminMapper.findAllProfiles();
        for (Admin admin : admins) {
            UsersAuth usersAuth = usersAuthMapper.findByUserId(admin.getUserId());
            if (usersAuth != null) {
                admin.setAuthorities(usersAuth.getAuthorities());
            }
        }
        return admins;
    }

    // 회원 삭제
    @Transactional
    public void deleteProfileByUserId(String userId) {
        adminMapper.deleteProfileByUserId(userId);
    }

    // 회원 선택 삭제
    @Transactional
    public void deleteProfilesByUserIds(List<String> userIds) {
        for (String userId : userIds) {
            adminMapper.deleteProfilesByUserIds(userIds);
        }
    }

    // 관리자 권한 (부여/회수)
    @Transactional
    public void toggleAdmin(String userId, String newRole) {
        usersAuthMapper.updateUserRole(userId, newRole);
    }

    // 로그인 (잠금/해제)
    @Transactional
    public void toggleLoginLock(String userId, boolean newLockStatus) {
        adminMapper.toggleLoginLock(userId, newLockStatus);
    }

    // 필터 및 검색 조건에 따른 회원 조회
    public List<Admin> getFilteredUsers(String role, Boolean loginLock, String keyword) {
        return adminMapper.findFilteredProfiles(role, loginLock, keyword);
    }
}


