package com.elice.boardproject.activity.service;

import com.elice.boardproject.post.entity.PostDto;
import com.elice.boardproject.activity.mapper.ActivityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ActivityService {
    @Autowired
    private ActivityMapper activityMapper;

    // 작성한 게시물 조회
    public List<PostDto> findPostsByUserId(String userId, int page, int pageSize) {

        int offset = (page - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        return activityMapper.findPostsByUserId(params);
    }

    // 즐겨찾기한 게시물 조회
    public List<PostDto> findScrapsByUserId(String userId, int page, int pageSize) {

        int offset = (page - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        return activityMapper.findScrapsByUserId(params);
    }
}
