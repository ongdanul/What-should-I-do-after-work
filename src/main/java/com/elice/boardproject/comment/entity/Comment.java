package com.elice.boardproject.comment.entity;

import java.time.Instant;

public class Comment {

    private Long commentId;
    private String userId;
    private Long postId;
    private String commentContent;
    private Instant regDate;
}
