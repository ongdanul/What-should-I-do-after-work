package com.elice.boardproject.admin.mapper;

import com.elice.boardproject.admin.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminMapper {
    // 전체 회원 조회
    List<Admin> findAllProfiles();

    // 회원 삭제
    void deleteProfileByUserId(@Param("userId") String userId);

    // 로그인 (잠금/잠금 해제)
    void toggleLoginLock(@Param("userId") String userId, @Param("newLockStatus") boolean newLockStatus);
}
