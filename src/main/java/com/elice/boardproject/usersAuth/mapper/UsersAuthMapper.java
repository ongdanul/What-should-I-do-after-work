package com.elice.boardproject.usersAuth.mapper;

import com.elice.boardproject.usersAuth.entity.UsersAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersAuthMapper {
    public void registerUserAuth(String userId);
    UsersAuth findByUserId(String userId);
}
