package com.elice.boardproject.user.mapper;

import com.elice.boardproject.user.entity.Users;
import org.apache.ibatis.annotations.Mapper;

import java.time.Instant;

@Mapper
public interface UsersMapper {
    Boolean existsByUserId(String userId);
    public void registerUser(Users user);
    Users findByUserId(String userId);
    void loginLock (Users user);
    String findUser(String userName, String contact);
    Users findUserPw(String userName, String email);
    void updatePassword(String userName, String email, String userPw, Instant modDate);
}
