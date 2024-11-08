package com.elice.boardproject.user.mapper;

import com.elice.boardproject.user.entity.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {
    Boolean existsByUserId(String userId);
    public void registerUser(Users user);
    Users findByUserId(String userId);
    void loginLock (Users user);

    String findUser(String userName, String contact);

    String findPasswordHash(String userId);

}
