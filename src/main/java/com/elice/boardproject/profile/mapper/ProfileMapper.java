package com.elice.boardproject.profile.mapper;

import com.elice.boardproject.profile.entity.Profile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProfileMapper {
    // MY 회원 정보 조회
    Profile findProfileByUserId(String userId);

    /**
     * MY 회원 정보 수정 (로컬 회원)
     *
     * @param profile 수정할 사용자 프로필 정보
     */
    void updateProfileLocalUser(Profile profile);

    /**
     * MY 회원 정보 수정 (소셜 회원)
     *
     * @param profile 수정할 소셜 사용자 프로필 정보
     */
    void updateProfileSocialUser(Profile profile);

    // 비밀번호 찾기
    String findPassword(String userId);

    // MY 회원 탈퇴
    void deleteProfileByUserId(@Param("userId") String userId);
}

