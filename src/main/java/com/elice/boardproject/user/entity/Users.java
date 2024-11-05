package com.elice.boardproject.user.entity;

import java.time.Instant;

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
