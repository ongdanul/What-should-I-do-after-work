package com.elice.boardproject.admin.service;

import com.elice.boardproject.admin.entity.Admin;
import com.elice.boardproject.admin.mapper.AdminMapper;
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

    public List<Admin> getAllProfiles() {
        return adminMapper.findAllProfiles();
    }

    @Transactional
    public void deleteProfileByUserId(String userId) { //어드민 회원삭제
        adminMapper.deleteProfileByUserId(userId);
    }
}
    //로그인 잠금 토글 미완성
    /*@Transactional
    public void toggleLoginLock(String userId, boolean currentLockState) {
        profileMapper.toggleLoginLock(userId, !currentLockState);
    }*/


