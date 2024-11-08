package com.elice.boardproject.comment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    private Long commentId;
    private String userId;
    private Long postId;
    private String commentContent;
    private Instant regDate;
}
