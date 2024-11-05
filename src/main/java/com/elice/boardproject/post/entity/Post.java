package com.elice.boardproject.post.entity;

import java.time.Instant;

public class Post {
    private Long postId;
    private String userId;
    private Long boardId;
    private String postTitle;
    private String postContent;
    private Instant regDate;
    private Instant modDate;
    private Long view_count;
}
