package com.elice.boardproject.comment.entity;

import com.elice.boardproject.user.entity.Users;
import lombok.*;

import java.time.Instant;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto {

    private Long commentId;
    private String userId;
    private Long postId;
    private String commentContent;
    private Instant regDate;
    private Users user;

    public CommentResponseDto toCommentResponseDto() {
        return null;
    }
}