package com.elice.boardproject.users.service;

import com.elice.boardproject.users.dto.CustomUserDetails;
import com.elice.boardproject.users.entity.Users;
import com.elice.boardproject.users.mapper.UsersMapper;
import com.elice.boardproject.usersAuth.entity.UsersAuth;
import com.elice.boardproject.usersAuth.mapper.UsersAuthMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersMapper usersMapper;
    private final UsersAuthMapper usersAuthMapper;

    public CustomUserDetailsService(UsersMapper usersMapper, UsersAuthMapper usersAuthMapper) {
        this.usersMapper = usersMapper;
        this.usersAuthMapper = usersAuthMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername: {}", username);

        // DB에서 사용자 정보 찾기
        Users user = usersMapper.findByUserId(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // 사용자에 대한 권한 정보 찾기
        UsersAuth usersAuth = usersAuthMapper.findByUserId(user.getUserId());

        if (usersAuth == null) {
            throw new UsernameNotFoundException("User authorities not found");
        }

        // CustomUserDetails 객체를 생성하여 반환
        return new CustomUserDetails(user, usersAuth);
    }
}
