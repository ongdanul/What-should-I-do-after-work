package com.elice.boardproject.profile.service;

import com.elice.boardproject.profile.entity.Profile;
import com.elice.boardproject.profile.mapper.ProfileMapper;
import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.mapper.UsersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileMapper profileMapper;
    private final UsersMapper usersMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // MY 회원 정보 조회
    public Profile getProfileByUserId(String userId) {
        return profileMapper.findProfileByUserId(userId);
    }

    // MY 회원 정보 수정
    @Transactional
    public void updateProfile(Profile profile) {

        Users existingUser = usersMapper.findByUser(profile.getUserId());

        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        String userPw = profile.getUserPw();
        String encodedPassword = !StringUtils.hasText(userPw) ?
                existingUser.getUserPw() : bCryptPasswordEncoder.encode(userPw);

        profile.edit(
            profile.getUserId(),
            profile.getUserName(),
            profile.getContact(),
            profile.getEmail(),
            encodedPassword
        );

        profileMapper.updateProfile(profile);
    }

    // 비밀번호 확인
    public boolean checkPassword(String userId, String inputPassword) {
        String storedPassword = profileMapper.findPassword(userId);
        return bCryptPasswordEncoder.matches(inputPassword, storedPassword);
    }

    // MY 회원 탈퇴
    @Transactional
    public void deleteProfileByUserId(String userId) {
        profileMapper.deleteProfileByUserId(userId);
    }
}
