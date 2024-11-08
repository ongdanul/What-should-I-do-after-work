package com.elice.boardproject.post.entity;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostResponseDto {
    private String userId;
    private String postTitle;
    private String postContent;
    private Instant regDate;
    private Instant modDate;
    private Long viewCount;
}
