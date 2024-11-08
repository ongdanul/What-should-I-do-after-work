package com.elice.boardproject.post.mapper;

import com.elice.boardproject.post.entity.Post;
import com.elice.boardproject.post.entity.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    List<Post> findAll(long boardId);

    List<Post> postFilter(String filter, String description, long boardId);

    Post detail(Long postId);

    int insert(Post post);      // 등록

    int update(Post post);      // 수정

    void delete(Post post);      // 삭제
}
