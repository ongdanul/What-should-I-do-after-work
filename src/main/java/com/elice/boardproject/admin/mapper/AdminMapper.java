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

    // 회원 선택 삭제
    void deleteProfilesByUserIds(@Param("userIds") List<String> userIds);

    // 로그인 (잠금/잠금 해제)
    void toggleLoginLock(@Param("userId") String userId, @Param("newLockStatus") boolean newLockStatus);

    // 필터 및 검색 조건에 따른 회원 조회
    List<Admin> findFilteredProfiles(
            @Param("role") String role,
            @Param("loginLock") Boolean loginLock,
            @Param("keyword") String keyword
    );
}
