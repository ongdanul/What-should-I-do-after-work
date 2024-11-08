package com.elice.boardproject.profile.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Profile {
    private String userId;
    private String userName;
    private String contact;
    private String email;
    private String userPw; //회원정보 수정을 위한 비밀번호 필드 추가
}
