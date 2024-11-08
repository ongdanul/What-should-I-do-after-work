package com.elice.boardproject.post.mapper;

import com.elice.boardproject.post.entity.Post;
import com.elice.boardproject.post.entity.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    // 전체 조회
    List<PostDto> findAll(long boardId);

    // 필터
    List<PostDto> postFilter(String filter, String description, long boardId);

    // 단건 조회
    PostDto detail(Long postId);

    //등록
    int insert(PostDto post);

    // 수정
    int update(PostDto post);

    // 삭제
    void delete(PostDto post);
}
