package com.elice.boardproject.user.dto;

import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.usersAuth.entity.UsersAuth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Users user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Users user, UsersAuth usersAuth) {
        this.user = user;
        this.authorities = buildAuthorities(usersAuth.getAuthorities());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return user.getUserPw();
    }
    @Override
    public String getUsername() {
        return user.getUserId();
    }

    private List<GrantedAuthority> buildAuthorities(String authoritiesString) {
        List<GrantedAuthority> authoritiesList = new ArrayList<>();
        if (authoritiesString != null && !authoritiesString.isEmpty()) {
            String[] authoritiesArray = authoritiesString.split(",");
            for (String authority : authoritiesArray) {
                authoritiesList.add(new SimpleGrantedAuthority(authority.trim()));
            }
        }
        return authoritiesList;
    }
}
