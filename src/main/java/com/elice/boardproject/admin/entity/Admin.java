package com.elice.boardproject.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    private String userId;
    private String userName;
    private String contact;
    private String email;
    private String userPw;  // 비밀번호 필드 추가
    private Instant modDate;
    private Instant regDate; // 가입일
    private boolean loginLock; // 로그인 잠금 여부
    @Setter
    private String authorities;

}

