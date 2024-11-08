package com.elice.boardproject.user.service;

import com.elice.boardproject.user.dto.SignUpDTO;
import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.mapper.UsersMapper;
import com.elice.boardproject.usersAuth.mapper.UsersAuthMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersMapper usersMapper;

    private final UsersAuthMapper usersAuthMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Transactional
    public boolean signUpProcess(@Valid SignUpDTO signUpDTO) {

        Boolean isExist = usersMapper.existsByUserId(signUpDTO.getUserId());

        if (isExist) {
            return false;
        }

        log.info("1" + signUpDTO.getUserPw());

        signUpDTO.setUserPw(bCryptPasswordEncoder.encode(signUpDTO.getUserPw()));
        Users user = signUpDTO.toEntity();

        log.info("2" + signUpDTO.getUserPw());

        usersMapper.registerUser(user);

        usersAuthMapper.registerUserAuth(user.getUserId());

        return true;
    }

    public Boolean isUserIdExists(String userId) {

        return usersMapper.existsByUserId(userId);
    }

    public String findUser(String userName, String contact) {

        return usersMapper.findUser(userName, contact);

    }
    public boolean checkPassword(String userId, String inputPassword) {

        String storedPasswordHash = usersMapper.findPasswordHash(userId);
        return bCryptPasswordEncoder.matches(inputPassword, storedPasswordHash);

    }


}
