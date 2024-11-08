package com.elice.boardproject.comment.mapper;

import com.elice.boardproject.comment.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> findComment(Long postId);
}
