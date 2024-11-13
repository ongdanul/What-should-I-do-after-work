package com.elice.boardproject.follow.service;

import com.elice.boardproject.follow.mapper.FollowMapper;
import com.elice.boardproject.post.entity.PostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {
    private final FollowMapper followMapper;

    public FollowService(FollowMapper followMapper) {
        this.followMapper = followMapper;
    }

    public List<PostDto> findFollow(String userId) {
        return followMapper.findFollow(userId);
    }

    public List<PostDto> findFollower(String userId) {
        return followMapper.findFollower(userId);
    }
}
