package com.elice.boardproject.user.service;

import com.elice.boardproject.user.dto.SignUpDTO;
import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.mapper.UsersMapper;
import com.elice.boardproject.usersAuth.mapper.UsersAuthMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersMapper usersMapper;

    private final UsersAuthMapper usersAuthMapper;

    private final JavaMailSender javaMailSender;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Transactional
    public boolean signUpProcess(@Valid SignUpDTO signUpDTO) {

        Boolean isExist = usersMapper.existsByUserId(signUpDTO.getUserId());

        if (isExist) {
            return false;
        }

        signUpDTO.setUserPw(bCryptPasswordEncoder.encode(signUpDTO.getUserPw()));
        Users user = signUpDTO.toEntity();

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

    public String findPw(String userName, String email) {

        Users user = usersMapper.findUserPw(userName, email);

        if (user != null) {

            String newPassword = createNewPassword();

            user.setLoginLock(false);

            usersMapper.loginLock(user);

            usersMapper.updatePassword(userName, email, bCryptPasswordEncoder.encode(newPassword), Instant.now());
            sendNewPasswordByMail(email, newPassword);

            return "비밀번호가 이메일로 발송되었습니다.";
        }

        return "사용자 정보를 찾을 수 없습니다.";
    }

    private String createNewPassword() {

        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuffer password  = new StringBuffer();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < 8; i++) {
            int index = secureRandom.nextInt(chars.length);
            char selectedChar = (index % 2 == 0) ? Character.toUpperCase(chars[index]) : chars[index];
            password.append(selectedChar);
        }
        return password.toString();
    }

    private void sendNewPasswordByMail(String toMailAddr, String newPassword) {
        try {
            final MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {

                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(toMailAddr);
                mimeMessageHelper.setSubject("[퇴근하고 뭐하지?] 새 비밀번호 안내입니다.");
                mimeMessageHelper.setText("새 비밀번호 : " + newPassword, true);

            };

            javaMailSender.send(mimeMessagePreparator);
        } catch (Exception e) {
            log.error("이메일 발송 중 오류 발생: {}", e.getMessage());
        }

    }
}
