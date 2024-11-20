package com.elice.boardproject.user.controller;

import com.elice.boardproject.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/find-id")
    public ResponseEntity<String> findId(@RequestBody Map<String, String> requestBody) {

        String userName = requestBody.get("userName");
        String contact = requestBody.get("contact");

        String userId = userService.findByUserId(userName, contact);

        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

        @PostMapping("/find-pw")
        public ResponseEntity<String> findPw(@RequestBody Map<String, String> requestBody) {
            String userName = requestBody.get("userName");
            String userId = requestBody.get("userId");

            String resultMessage = userService.findByUserdPw(userName, userId);

            if (resultMessage.equals("임시 비밀번호 발급이 완료되었습니다.")) {
                return ResponseEntity.ok(resultMessage);
            } else if (resultMessage.equals("사용자 정보를 찾을 수 없습니다.")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMessage);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
            }
        }
}