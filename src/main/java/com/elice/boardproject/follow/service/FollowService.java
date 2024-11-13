package com.elice.boardproject.follow.service;

import com.elice.boardproject.follow.mapper.FollowMapper;
import com.elice.boardproject.post.entity.PostDto;
import com.elice.boardproject.user.entity.Users;
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

    public List<Users> findFollower(String userId) {
        return followMapper.findFollower(userId);
    }

    public int checkFollow(String myId, String userId) { return followMapper.checkFollow(myId, userId); }

    public int insert(String myId, String followId) { return followMapper.insert(myId, followId); }

    public int delete(String myId, String followId) { return followMapper.delete(myId, followId); }
}
