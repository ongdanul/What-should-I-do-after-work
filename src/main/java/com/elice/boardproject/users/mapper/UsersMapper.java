package com.elice.boardproject.users.mapper;

import com.elice.boardproject.users.entity.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {
    Boolean existsByUserId(String userId);
    public void registerUser(Users user);
    Users findByUserId(String userId);
    void loginLock (Users user);
    String findPasswordHash(String userId);
}
