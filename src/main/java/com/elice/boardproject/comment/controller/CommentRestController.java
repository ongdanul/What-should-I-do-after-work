package com.elice.boardproject.comment.controller;

import com.elice.boardproject.comment.entity.CommentDto;
import com.elice.boardproject.comment.entity.CommentRequestDto;
import com.elice.boardproject.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class CommentRestController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comment/{postId}")
    public ResponseEntity<List<CommentDto>> getComment(@PathVariable Long postId){
        List<CommentDto> comments =  commentService.findComment(postId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // 댓글 추가하기
    @PostMapping("/comment/add")
    public ResponseEntity<List<CommentDto>> addComment(@RequestBody CommentRequestDto commentRequestDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }
        commentRequestDto.setUserId(userId);

        int resultNo = commentService.addComment(commentRequestDto);

        if(resultNo == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<CommentDto> comments =  commentService.findComment(commentRequestDto.getPostId());

        if(comments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(comments, HttpStatus.CREATED);
    }
    // 댓글 수정하기
    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<Optional<CommentDto>> editComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto) {
        String content = commentRequestDto.getCommentContent();

        Optional<CommentDto> comment= commentService.updateComment(commentId, content);

        if(comment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }
    // 댓글 삭제하기
    @DeleteMapping("/comment/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}