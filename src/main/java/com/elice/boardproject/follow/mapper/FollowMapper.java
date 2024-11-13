package com.elice.boardproject.follow.mapper;

import com.elice.boardproject.post.entity.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowMapper {
    List<PostDto> findFollow(String userId);

    List<PostDto> findFollower(String userId);

    int checkFollow(String myId, String userId);

    int insert(String myId, String userId);

    int delete(String myId, String userId);
}