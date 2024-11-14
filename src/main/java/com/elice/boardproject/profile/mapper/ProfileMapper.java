package com.elice.boardproject.profile.mapper;

import com.elice.boardproject.profile.entity.Profile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProfileMapper {
    // MY 회원 정보 조회
    Profile findProfileByUserId(String userId);

    // MY 회원 정보 수정
    void updateProfile(Profile profile);

    String findPassword(String userId);

    // MY 회원 탈퇴
    void deleteProfileByUserId(@Param("userId") String userId);
}

