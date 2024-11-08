package com.elice.boardproject.profile.mapper;
import com.elice.boardproject.profile.entity.Profile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProfileMapper {
    Profile findProfileByUserId(String userId);

    void updateProfile(Profile profile);

    //회원탈퇴 미완성
    //void deleteProfileByUserId(@Param("userId") String userId);
}
