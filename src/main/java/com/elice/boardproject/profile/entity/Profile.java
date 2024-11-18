package com.elice.boardproject.profile.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    private String userId;

    private String userName;

    @NotBlank(message = "Contact is required")
    private String contact;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email should match the required pattern")
    private String email;

    @Size(min = 8, max = 13, message = "Password must be between 8 and 13 characters")
    private String userPw; //회원정보 수정을 위한 비밀번호 필드 추가

    public void edit (String userId, String userName, String contact, String email, String userPw) {
        this.userId = userId;
        this.userName = userName;
        this.contact = contact;
        this.email = email;
        this.userPw = userPw;
    }
}
