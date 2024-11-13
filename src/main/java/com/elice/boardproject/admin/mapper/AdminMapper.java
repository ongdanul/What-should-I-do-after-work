package com.elice.boardproject.admin.mapper;

import com.elice.boardproject.admin.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminMapper {
    List<Admin> findAllProfiles();

    void deleteProfileByUserId(@Param("userId") String userId);

    void toggleLoginLock(@Param("userId") String userId, @Param("newLockStatus") boolean newLockStatus);
}
