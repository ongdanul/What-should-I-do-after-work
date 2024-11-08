package com.elice.boardproject.user.service;

import com.elice.boardproject.user.entity.Profile;
import com.elice.boardproject.user.mapper.ProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final ProfileMapper profileMapper;

    @Autowired
    public ProfileService(ProfileMapper profileMapper) {
        this.profileMapper = profileMapper;
    }

    public Profile getProfileByUserId(String userId) {
        return profileMapper.findProfileByUserId(userId);
    }

    public void updateProfile(Profile profile) {
        profileMapper.updateProfile(profile);
    }
}
