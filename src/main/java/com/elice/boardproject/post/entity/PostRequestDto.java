package com.elice.boardproject.post.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostRequestDto {
    private String userId;
    private String postTitle;
    private String postContent;

    public PostDto toPostDto() {
        return PostDto.builder()
                .userId(userId)
                .postTitle(postTitle)
                .postContent(postContent)
                .build();
    }
}
