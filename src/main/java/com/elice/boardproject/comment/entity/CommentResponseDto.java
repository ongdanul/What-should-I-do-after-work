package com.elice.boardproject.comment.entity;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentResponseDto {

    private Long commentId;
    private Long userId;
    private String commentContent;
    private Instant regDate;
}