package com.elice.boardproject.user.mapper;

import com.elice.boardproject.user.entity.Users;
import org.apache.ibatis.annotations.Mapper;

import java.time.Instant;
import java.util.List;

@Mapper
public interface UsersMapper {
    Boolean existsByUserId(String userId);
    long countUserIds(String userName, String contact);
    List<Users> findByUserNameAndContact(String userName, String contact);
    Boolean existsByUserIdAndUserName(String userName, String userId);
    public void registerUser(Users user);
    Users findByUser(String userId);
    void editLoginLock(Users user);
    String findByUserId(String userName, String contact);
    String findByEmail(String userId);
    void editUserPw(String email, String userPw, Instant modDate, boolean loginLock);
}
