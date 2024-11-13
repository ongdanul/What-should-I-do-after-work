package com.elice.boardproject.follow.entity;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Follow {
    private String follow;
    private String follower;
    private Instant regDate;
}