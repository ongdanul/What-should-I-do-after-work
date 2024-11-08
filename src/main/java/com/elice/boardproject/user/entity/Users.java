package com.elice.boardproject.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
