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
    private String userPw;
}
