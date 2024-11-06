package com.elice.boardproject.post.entity;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Post {
    private Long postId;
    private String userId;
    private Long boardId;
    private String postTitle;
    private String postContent;
    private Instant regDate;
    private Instant modDate;
    private Long viewCount;

    public PostDto toPostDto() {
        return PostDto.builder()
                .userId(userId)
                .postTitle(postTitle)
                .postContent(postContent)
                .build();
    }
}
