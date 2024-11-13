package com.elice.boardproject.admin.service;

import com.elice.boardproject.admin.entity.Admin;
import com.elice.boardproject.admin.mapper.AdminMapper;
import com.elice.boardproject.usersAuth.entity.UsersAuth;
import com.elice.boardproject.usersAuth.mapper.UsersAuthMapper;
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

    @Transactional
    public void deleteProfileByUserId(String userId) {
        adminMapper.deleteProfileByUserId(userId);
    }

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

    @Transactional
    public void toggleAdmin(String userId, String newRole) {
        usersAuthMapper.updateUserRole(userId, newRole);
    }

    @Transactional
    public void toggleLoginLock(String userId, boolean newLockStatus) {
        adminMapper.toggleLoginLock(userId, newLockStatus);
    }
}


