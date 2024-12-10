package com.elice.boardproject.user.dto;

import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.entity.UsersAuth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 사용자 인증 정보를 담고 있는 CustomUserDetails 클래스입니다.
 * Spring Security에서 사용자 인증을 처리하기 위해 UserDetails 인터페이스를 구현합니다.
 */
public class CustomUserDetails implements UserDetails {

    private final Users user;
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * 사용자 정보와 권한 정보를 바탕으로 CustomUserDetails 객체를 생성합니다.
     * @param user 사용자 정보를 포함한 Users 객체
     * @param usersAuth 사용자 권한 정보를 포함한 UsersAuth 객체
     */
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
        return user.getUserPassword();
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
