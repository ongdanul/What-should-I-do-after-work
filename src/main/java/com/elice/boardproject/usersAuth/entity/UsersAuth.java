package com.elice.boardproject.usersAuth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersAuth {

    private String userId;

    private String authorities;
}
