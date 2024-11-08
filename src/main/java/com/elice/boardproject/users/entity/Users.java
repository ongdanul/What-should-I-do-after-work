package com.elice.boardproject.user.entity;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Users {
    private String userId;
    private String userPw;
    private String userName;
    private String contact;
    private String email;
    private Instant regDate;
    private Instant modDate;
    private boolean loginLock;
}
