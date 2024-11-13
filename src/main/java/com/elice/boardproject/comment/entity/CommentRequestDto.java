package com.elice.boardproject.comment.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CommentRequestDto {

    private Long commentId;
    private String userId;
    private Long postId;
    private String commentContent;

    public CommentDto toCommentDto() {
        return CommentDto.builder()
                .commentId(commentId)
                .userId(userId)
                .postId(postId)
                .commentContent(commentContent)
                .build();
    }
}