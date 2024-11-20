package com.elice.boardproject.user.mapper;

import com.elice.boardproject.user.entity.UsersAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsersAuthMapper {
    void registerUserAuth(String userId);
    UsersAuth findByUserId(String userId);
    void updateUserRole(@Param("userId") String userId, @Param("newRole") String newRole);
}
