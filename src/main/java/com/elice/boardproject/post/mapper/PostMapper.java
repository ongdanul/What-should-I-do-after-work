package com.elice.boardproject.post.mapper;

import com.elice.boardproject.post.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    List<Post> findAll();
}
