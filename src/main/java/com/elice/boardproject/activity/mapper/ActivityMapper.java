package com.elice.boardproject.activity.mapper;

import com.elice.boardproject.post.entity.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ActivityMapper {
    List<PostDto> findPostsByUserId(Map<String, Object> params);
    List<PostDto> findScrapsByUserId(Map<String, Object> params);
}
