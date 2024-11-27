package com.elice.boardproject.user.service;

import com.elice.boardproject.user.dto.CustomUserDetails;
import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.mapper.UsersMapper;
import com.elice.boardproject.user.entity.UsersAuth;
import com.elice.boardproject.user.mapper.UsersAuthMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

        Users user = usersMapper.findByUser(username);
        log.info("Test - CustomUserDetailsService : userName: {}", username);
        log.info("Test - CustomUserDetailsService : user: {}", user);
        log.info("Test - CustomUserDetailsService : userId: {}, userPassword: {}", user.getUserId(), user.getUserPassword());

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UsersAuth usersAuth = usersAuthMapper.findByUserId(user.getUserId());
        log.info("Test - CustomUserDetailsService : usersAuth: {}", usersAuth);

        if (usersAuth == null) {
            throw new UsernameNotFoundException("User authorities not found");
        }

        return new CustomUserDetails(user, usersAuth);
    }
}
