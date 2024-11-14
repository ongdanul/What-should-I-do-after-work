package com.elice.boardproject.follow.mapper;

import com.elice.boardproject.post.entity.PostDto;
import com.elice.boardproject.user.entity.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FollowMapper {
    List<PostDto> findFollow(Map<String, Object> params);

    List<Users> findFollower(Map<String, Object> params);

    int checkFollow(String myId, String userId);

    int insert(String myId, String userId);

    int delete(String myId, String userId);
}