package com.elice.boardproject.global.config;

import com.elice.boardproject.user.entity.Users;
import com.elice.boardproject.user.mapper.UsersMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFailureResetScheduler {
    private final UsersMapper usersMapper;
    private static final long RESET_PERIOD = 3 * 24 * 60 * 60 * 1000L; //3days

    /**
     * 로그인 실패 횟수를 초기화하는 스케줄러 메소드.
     * 매일 자정(00:00:00)에 실행되며, 로그인 실패 횟수를 초기화합니다.
     * 테스트용으론 매 분 실행하려면 cron = "0 0/1 * * * ?"을 사용.
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
