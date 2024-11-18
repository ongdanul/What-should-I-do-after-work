package com.elice.boardproject.comment.mapper;

import com.elice.boardproject.comment.entity.CommentDto;
import com.elice.boardproject.comment.entity.CommentRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMapper {
    // 전체 조회
    List<CommentDto> findComment(Long postId);

    Optional<CommentDto> findCommentDetail(Long commentId);

    // 등록
    int insert(CommentRequestDto commentRequestDto);

    // 수정
    int update(Long commentId, String content);

    // 삭제
    int delete(Long commentId);
}