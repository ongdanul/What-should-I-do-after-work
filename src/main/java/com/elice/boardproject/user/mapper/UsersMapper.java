package com.elice.boardproject.user.mapper;

import com.elice.boardproject.user.dto.SignUpDTO;
import com.elice.boardproject.user.entity.Users;
import org.apache.ibatis.annotations.Mapper;

import java.time.Instant;
import java.util.List;

@Mapper
public interface UsersMapper {
    /**
     * 특정 userId가 이미 존재하는지 확인합니다.
     * @param userId 확인할 userId
     * @return 해당 userId가 존재하면 true, 그렇지 않으면 false
     */
    Boolean existsByUserId(String userId);

    /**
     * 주어진 사용자 이름과 연락처를 기반으로 등록된 계정 수를 확인합니다.
     * @param userName 사용자 이름
     * @param contact 사용자 연락처
     * @return 주어진 조건에 맞는 사용자 계정 수
     */
    long countUserIds(String userName, String contact);

    /**
     * 새 사용자 계정을 등록합니다. (로컬 회원가입)
     * @param signUpDTO 사용자 정보를 포함한 DTO 객체
     */
    void registerUser(SignUpDTO signUpDTO);

    /**
     * 새 사용자 계정을 등록합니다. (소셜 회원가입)
     * @param user 사용자 정보를 포함한 Users 객체
     */
    void registerOauthUser(Users user);

    /**
     * 사용자 이름과 연락처를 기반으로 userId를 조회합니다.
     * @param userName 사용자 이름
     * @param contact 사용자 연락처
     * @return 해당 조건에 맞는 userId 리스트
     */
    List<String> findByUserId(String userName, String contact);

    /**
     * 주어진 사용자 이름과 userId가 일치하는 사용자가 있는지 확인합니다.
     * @param userName 사용자 이름
     * @param userId 사용자 ID
     * @return 해당 조건에 맞는 사용자가 존재하면 true, 아니면 false
     */
    Boolean existsByUserIdAndUserName(String userName, String userId);

    /**
     * 주어진 userId로 이메일을 조회합니다.
     * @param userId 사용자 ID
     * @return 해당 userId의 이메일
     */
    String findByEmail(String userId);

    /**
     * userId로 사용자의 비밀번호를 수정하거나 초기화합니다.
     * @param userId 사용자 ID
     * @param userPassword 새 비밀번호
     * @param modDate 수정 일시
     * @param loginLock 로그인 잠금 상태
     */
    void editUserPassword(String userId, String userPassword, Instant modDate, boolean loginLock, int loginAttempts);

    /**
     * 주어진 userId로 사용자 정보를 조회합니다.
     * @param userId 사용자 ID
     * @return 해당 userId에 해당하는 Users 객체
     */
    Users findByUser(String userId);

    /**
     * 로그인 잠금 상태를 업데이트합니다.
     * @param user 업데이트할 사용자 정보
     */
    void editLoginLock(Users user);

    /**
     * 로그인 실패 횟수 초기화를 위해 사용자 정보를 조회합니다.
     * @return userId, loginLock, loginAttempts, lastFailedLogin
     */
    List<Users> findLoginstatus();
}
