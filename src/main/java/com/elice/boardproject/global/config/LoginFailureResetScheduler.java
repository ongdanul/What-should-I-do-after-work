package com.elice.boardproject.global.config;

import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class LoginFailureResetScheduler {
    private final UsersMapper usersMapper;

    public LoginFailureResetScheduler(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    private static final long RESET_PERIOD = 3 * 24 * 60 * 60 * 1000L; //3days

    /**
     * 매일 자정(00:00:00)에 실패 횟수를 초기화
     * 테스트 - 매 분 초기화 (cron = "0 0/1 * * * ?")
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetLoginAttempts() {
        log.debug("Login failure count reset scheduler started");

        List<Users> users = usersMapper.findLoginstatus();
        long now = System.currentTimeMillis();

        users.forEach(user -> {
            // 로그인 실패 횟수 초기화 조건:
            // - 로그인 잠금 상태가 아닌 경우
            // - 마지막 실패 시간에서 3일 이상 지난 경우
            // - LoginAttempts가 5 미만일 경우에만 초기화
            if (!user.isLoginLock() && user.getLastFailedLogin() != null &&
                    now - user.getLastFailedLogin().toEpochMilli() > RESET_PERIOD) {
                if (user.getLoginAttempts() < 5) {
                    user.setLoginAttempts(0);
                    try {
                        usersMapper.editLoginLock(user);
                        log.info("Test - 사용자 {} 실패 횟수 초기화 완료", user.getUserId()); //TODO 로그인 기능 완성후 삭제하기
                    } catch (Exception e) {
                        log.error("error occurred while resetting failure count for user", e);
                    }
                }
            }
        });
        log.debug("Login failure count reset scheduler completed");
    }
}
