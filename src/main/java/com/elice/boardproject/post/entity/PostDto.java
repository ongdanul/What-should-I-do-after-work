package com.elice.boardproject.post.entity;

import com.elice.boardproject.follow.entity.Follow;
import com.elice.boardproject.scrap.entity.Scrap;
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
    private Scrap scrap;
    private Follow follow;
}
