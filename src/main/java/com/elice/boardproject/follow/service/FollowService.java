package com.elice.boardproject.follow.service;

import com.elice.boardproject.follow.mapper.FollowMapper;
import com.elice.boardproject.post.entity.PostDto;
import com.elice.boardproject.user.entity.Users;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FollowService {
    private final FollowMapper followMapper;

    public FollowService(FollowMapper followMapper) {
        this.followMapper = followMapper;
    }

    public List<PostDto> findFollow(String userId, int page, int pageSize) {

        int offset = (page - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        return followMapper.findFollow(params);
    }

    public List<Users> findFollower(String userId, int page, int pageSize) {

        int offset = (page - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        return followMapper.findFollower(params);
    }

    public int checkFollow(String myId, String userId) { return followMapper.checkFollow(myId, userId); }

    public int insert(String myId, String followId) { return followMapper.insert(myId, followId); }

    public int delete(String myId, String followId) { return followMapper.delete(myId, followId); }
}
