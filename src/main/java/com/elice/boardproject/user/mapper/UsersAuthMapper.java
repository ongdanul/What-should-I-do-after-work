package com.elice.boardproject.user.mapper;

import com.elice.boardproject.user.entity.UsersAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsersAuthMapper {
    void registerUserAuth(String userId);

    /**
     * 사용자 ID를 기반으로 해당 사용자의 인증 정보를 조회합니다.
     * @param userId 사용자 ID
     * @return 해당 userId에 대한 UsersAuth 객체
     */
    UsersAuth findByUserId(String userId);
    void updateUserRole(@Param("userId") String userId, @Param("newRole") String newRole);
}
