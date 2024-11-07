package com.elice.boardproject.user.entity;

public class Profile {
    private String userId;
    private String userName;
    private String contact;
    private String email;

    // 기본 생성자
    public Profile() {}

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
