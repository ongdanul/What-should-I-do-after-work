package com.elice.boardproject.user.service;

import com.elice.boardproject.user.dto.CustomUserDetails;
import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.mapper.UsersMapper;
import com.elice.boardproject.user.entity.UsersAuth;
import com.elice.boardproject.user.mapper.UsersAuthMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증을 위해 커스텀 UserDetailsService를 구현한 서비스 클래스입니다.
 * 사용자 정보를 로드하여 Spring Security의 UserDetails로 반환합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersMapper usersMapper;
    private final UsersAuthMapper usersAuthMapper;

    /**
     * 사용자 이름을 기반으로 사용자 정보를 로드합니다.
     * @param username 사용자의 아이디입니다.
     * @return UserDetails 사용자 정보와 권한을 포함하는 CustomUserDetails 객체
     * @throws UsernameNotFoundException 사용자 정보가 없을 경우 예외 발생
     */
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
