package com.elice.boardproject.admin.mapper;

import com.elice.boardproject.admin.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminMapper {

    List<Admin> findAllProfiles();

    /*void deleteProfileByUserId(@Param("userId") String userId); // XML 매핑 파일에서 프로필 삭제*/
}

//로그인 잠금 토글 미완성
    //void toggleLoginLock(@Param("userId") String userId, @Param("loginLock") boolean loginLock); // 로그인 잠금 토글


