package com.elice.boardproject.user.mapper;

import com.elice.boardproject.user.entity.Profile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProfileMapper {
    @Select("SELECT user_id, user_name, contact, email FROM users WHERE user_id = #{userId}")
    Profile findProfileByUserId(String userId);

    @Update("UPDATE users SET user_name = #{userName}, contact = #{contact}, email = #{email} WHERE user_id = #{userId}")
    void updateProfile(Profile profile);
}
