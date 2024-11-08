package com.elice.boardproject.post.entity;

import com.elice.boardproject.user.entity.Users;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PostDto {
    private Long postId;
    private String userId;
    private Long boardId;
    private String postTitle;
    private String postContent;
    private Instant regDate;
    private Instant modDate;
    private Long viewCount;
    private Users user;

    public PostResponseDto toPostResponseDto() {
        return PostResponseDto.builder()
                .userId(userId)
                .postTitle(postTitle)
                .postContent(postContent)
                .regDate(regDate)
                .modDate(modDate)
                .viewCount(viewCount)
                .build();
    }
}
