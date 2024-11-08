package com.elice.boardproject.comment.service;

import com.elice.boardproject.comment.entity.Comment;
import com.elice.boardproject.comment.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired private CommentMapper commentMapper;

    public List<Comment> findComment(Long postId) {
        return commentMapper.findComment(postId);
    }
}
