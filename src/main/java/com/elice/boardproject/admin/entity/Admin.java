package com.elice.boardproject.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    private String userId;
    private String userName;
    private String contact;
    private String email;
    private String userPw;
    private Instant modDate;
    private Instant regDate;
    private boolean loginLock;
    @Setter
    private String authorities;

    //가입날짜 포매팅
    public String getRegDateFormatted() {
        if (regDate == null) return "";
        LocalDateTime localDateTime = LocalDateTime.ofInstant(regDate, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a h:mm");
        return localDateTime.format(formatter);
    }

}

