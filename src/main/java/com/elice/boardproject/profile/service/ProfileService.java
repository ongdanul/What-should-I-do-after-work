package com.elice.boardproject.profile.service;

import com.elice.boardproject.file.FileHandler;
import com.elice.boardproject.profile.entity.Profile;
import com.elice.boardproject.profile.mapper.ProfileMapper;
import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.mapper.UsersMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileMapper profileMapper;
    private final UsersMapper usersMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FileHandler fileHandler;

    @Value("${profileUploadPath}")
    private String PROFILE_UPLOAD_PATH;

    // MY 회원 정보 조회
    public Profile getProfileByUserId(String userId) {
        return profileMapper.findProfileByUserId(userId);
    }

    /**
     * 로컬 사용자의 프로필 정보를 수정합니다.
     * @param profile 수정할 사용자 프로필 정보
     * @throws IllegalArgumentException 사용자 정보가 존재하지 않으면 예외 발생
     */
    @Transactional
    public void updateLocalUserProfile(Profile profile) {

        Users existingUser = usersMapper.findByUser(profile.getUserId());

        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        String userPassword = profile.getUserPassword();
        String encodedPassword = !StringUtils.hasText(userPassword) ?
                existingUser.getUserPassword() : bCryptPasswordEncoder.encode(userPassword);

        profile.editLocalUser(
            profile.getUserId(),
            profile.getUserName(),
            profile.getContact(),
            profile.getEmail(),
            encodedPassword
        );

        profileMapper.updateProfileLocalUser(profile);
    }

    /**
     * 소셜 로그인 사용자의 프로필 정보를 수정합니다.
     * @param profile 수정할 사용자 프로필 정보
     * @throws IllegalArgumentException 사용자 정보가 존재하지 않으면 예외 발생
     */
    @Transactional
    public void updateSocialUserProfile(Profile profile) {

        Users existingUser = usersMapper.findByUser(profile.getUserId());

        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        profile.editSocialUser(
                profile.getContact()
        );

        profileMapper.updateProfileSocialUser(profile);
    }

    /**
     * 회원 정보 수정 - 프로필 사진을 업로드합니다.
     * @param userId 사용자 ID (이메일 주소)
     * @param file 업로드할 프로필 이미지 파일
     * @throws IOException 파일 처리 중 오류 발생 시 예외 발생
     */
    public void uploadProfileImage(String userId, MultipartFile file) throws IOException {
        Users user = usersMapper.findByUser(userId);
        if (user==null){
            new RuntimeException("User not found");
        }

//        fileHandler.deleteFile(PROFILE_UPLOAD_PATH, user.getProfileUrl());
//
//        String newFileName = fileHandler.saveFile(PROFILE_UPLOAD_PATH, file);
//
//        user.setProfileUrl(newFileName);
//        profileMapper.save(user);
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
