package com.elice.boardproject.profile.service;

import com.elice.boardproject.profile.entity.Profile;
import com.elice.boardproject.profile.mapper.ProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileMapper profileMapper;

    public Profile getProfileByUserId(String userId) {
        return profileMapper.findProfileByUserId(userId);
    }

    @Transactional
    public void updateProfile(Profile profile) {
        profileMapper.updateProfile(profile);
    }

    @Transactional
    public void deleteProfileByUserId(String userId) {
        profileMapper.deleteProfileByUserId(userId);
    }
}
