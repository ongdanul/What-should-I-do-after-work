package com.elice.boardproject.scrap.entity;

import com.elice.boardproject.post.entity.PostDto;
import com.elice.boardproject.user.entity.Users;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ScrapDto {
    private Long scrapId;
    private String userId;
    private Long postId;
    private boolean exist;

    private PostDto post;
    private Users user;
}
