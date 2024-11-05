package com.elice.boardproject.post.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDto {
    private Long postId;
    private String userId;
    private Long boardId;
    private String postTitle;
    private String postContent;
    private Instant regDate;
    private Instant modDate;
    private Long view_count;
}
