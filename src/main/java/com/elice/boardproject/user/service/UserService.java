package com.elice.boardproject.user.service;

import com.elice.boardproject.file.FileHandler;
import com.elice.boardproject.user.dto.SignUpDTO;
import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.mapper.UsersMapper;
import com.elice.boardproject.user.mapper.UsersAuthMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersMapper usersMapper;
    private final UsersAuthMapper usersAuthMapper;
    private final JavaMailSender javaMailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원가입 처리를 진행합니다. 사용자 이름과 연락처를 기준으로 가입 가능한 최대 계정 수를 확인한 후,
     * 패스워드를 암호화하고 사용자 정보를 DB의 사용자 테이블과 사용자 인증 테이블에 각각 저장합니다.
     * @param signUpDTO 회원가입 정보를 담고 있는 DTO 객체
     * @return 회원가입이 성공적으로 처리되면 true, 그렇지 않으면 예외를 발생시킴
     */
    @Transactional
    public boolean signUpProcess(@Valid SignUpDTO signUpDTO) {
        final int MAX_USER_ACCOUNTS = 3;
        long countUserIds = countUserIds(signUpDTO.getUserName(), signUpDTO.getContact());
        if (countUserIds >= MAX_USER_ACCOUNTS) { //최대 가입 계정 수 3개 제한
            log.error("sign-up limit reached: userName={} contact={}", signUpDTO.getUserName(), signUpDTO.getContact());
            throw new RuntimeException("The maximum number of available IDs has been exceeded.");
        }

        signUpDTO.setUserPassword(bCryptPasswordEncoder.encode(signUpDTO.getUserPassword()));
        try {
            usersMapper.registerUser(signUpDTO);
            usersAuthMapper.registerUserAuth(signUpDTO.getUserId());
            return true;
        } catch (Exception e) {
            log.error("An error occurred during the sign-up process: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred during the sign-up process.");
        }
    }

    /**
     * 회원가입 시 주어진 userId가 이미 존재하는지 확인합니다.
     * @param userId 확인할 사용자 ID
     * @return 해당 userId가 존재하면 true, 그렇지 않으면 false
     */
    public Boolean isUserIdExists(String userId) {
        return usersMapper.existsByUserId(userId);
    }

    /**
     * 회원가입 시 사용자 이름과 연락처를 기반으로 등록된 사용자 계정 수를 확인합니다.
     * @param userName 사용자 이름
     * @param contact 사용자 연락처
     * @return 주어진 조건에 맞는 사용자 계정 수
     */
    public long countUserIds(String userName, String contact) {
        return usersMapper.countUserIds(userName, contact);
    }

    /**
     * 아이디 찾기 시 사용자 이름과 연락처를 기반으로 해당하는 userId 리스트를 조회합니다.
     * @param userName 사용자 이름
     * @param contact 사용자 연락처
     * @return 조건에 맞는 userId 리스트
     */
    public List<String> findByUserId(String userName, String contact) {
        return usersMapper.findByUserId(userName, contact);
    }

    /**
     * 비밀번호 찾기 시 주어진 userId, userName과 일치하는 사용자가 존재하는지 확인합니다.
     * userId를 기반으로 이메일을 조회하고, 임시 비밀번호를 발급하여 이메일로 발송합니다.
     * @param userName 사용자 이름
     * @param userId 사용자 ID
     * @return 임시 비밀번호 발급 상태 메시지
     */
    public String findByUserdPw(String userName, String userId) {
        boolean userExists = usersMapper.existsByUserIdAndUserName(userName, userId);

        if (userExists) {
            String email = usersMapper.findByEmail(userId);
            String newPassword = registerNewPassword();

            usersMapper.editUserPassword(userId, bCryptPasswordEncoder.encode(newPassword), Instant.now(), false, 0);
            sendNewPasswordByMail(email, newPassword);
            return "Temporary password issuance has been completed.";
        }

        return "User not found.";
    }

    /**
     * 비밀번호 찾기 시 주어진 userId와 userName이 일치하는 사용자가 존재하는지 확인합니다.
     * @param userId 사용자 ID
     * @param userName 사용자 이름
     * @return 해당 조건에 맞는 사용자가 존재하면 true, 아니면 false
     */
    public Boolean existsByUserIdAndUserName(String userId, String userName) {
        return usersMapper.existsByUserIdAndUserName(userId, userName);
    }

    /**
     * 비밀번호 찾기 - 임시비밀번호 발급 시 8자리 길이의 랜덤한 비밀번호를 생성합니다.
     * @return 생성된 임시 비밀번호
     */
    private String registerNewPassword() {

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

    /**
     * 비밀번호 찾기 - 임시비밀번호 발급 시 생성된 임시 비밀번호를 이메일로 발송합니다.
     * @param toMailAddr 수신자 이메일 주소
     * @param newPassword 임시 비밀번호
     */
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
            log.error("Error occurred while sending email: {}", e.getMessage());
        }
    }
}
