package com.elice.boardproject.comment.service;

import com.elice.boardproject.comment.entity.Comment;
import com.elice.boardproject.comment.entity.CommentDto;
import com.elice.boardproject.comment.entity.CommentRequestDto;
import com.elice.boardproject.comment.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired private CommentMapper commentMapper;
    // 댓글 전체 조회
    public List<CommentDto> findComment(Long postId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;

        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        return commentMapper.findComment(params);
    }

    // 댓글 단건 조회
    public Optional<CommentDto> findCommentDetail(Long postId) {
        return Optional.ofNullable(commentMapper.findCommentDetail(postId)
                .map(comment -> new CommentDto(comment.getCommentId(), comment.getUserId(), comment.getPostId(), comment.getCommentContent(), comment.getRegDate(), comment.getUser()))
                .orElse(null));
    }

    // 댓글 저장
    public int addComment(CommentRequestDto commentRequestDto) {
        return commentMapper.insert(commentRequestDto);
    }

    // 댓글 수정
    public Optional<CommentDto> updateComment(Long commentId, String content) {
        Optional<CommentDto> comments = commentMapper.findCommentDetail(commentId);

        if (comments.isEmpty()) {
            return comments;
        }

        int updatedComment = commentMapper.update(commentId, content);

        if (updatedComment == 0) {
            return comments;
        }

        CommentDto newCommentDto = comments.get();
        newCommentDto.setCommentContent(content);

        return Optional.of(newCommentDto);
    }
    // 댓글 삭제
    public int deleteComment(Long commentId) {
        return commentMapper.delete(commentId);
    }
}
