package com.elice.boardproject.admin.mapper;

import com.elice.boardproject.admin.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<Admin> findAllProfiles(); // 전체 회원 조회 메서드 추가
    void deleteProfileByUserId(@Param("userId") String userId);

    //로그인 잠금 토글 미완성
    //void toggleLoginLock(@Param("userId") String userId, @Param("loginLock") boolean loginLock); // 로그인 잠금 토글
}

