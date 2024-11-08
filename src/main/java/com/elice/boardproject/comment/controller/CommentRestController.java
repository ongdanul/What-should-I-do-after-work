package com.elice.boardproject.comment.controller;

import com.elice.boardproject.comment.service.CommentService;
import com.elice.boardproject.comment.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
public class CommentRestController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comment/{postId}")
    public ResponseEntity<List<Comment>> getComment(@PathVariable Long postId){
        List<Comment> comments =  commentService.findComment(postId);
        System.out.println("@@@@@@@@@@@@@@@@@@comments : " + comments);
        if(comments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

}
