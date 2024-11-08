package com.elice.boardproject.post.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostDto {
    private Long postId;
    private String userId;
    private Long boardId;
    private String postTitle;
    private String postContent;
//    private Instant regDate;
//    private Instant modDate;
//    private Long viewCount;

    public Post toPost() {
        return Post.builder()
                .postId(postId)
                .boardId(boardId)
                .userId(userId)
                .postTitle(postTitle)
                .postContent(postContent)
                .build();
    }
}
