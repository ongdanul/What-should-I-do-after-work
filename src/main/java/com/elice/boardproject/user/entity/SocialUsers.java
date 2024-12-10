package com.elice.boardproject.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialUsers {

    private long socialId;

    private String userId;

    private String provider;

    private String providerId;
}
