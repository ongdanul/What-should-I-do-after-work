package com.elice.boardproject.user.controller;

import com.elice.boardproject.profile.service.ProfileService;
import com.elice.boardproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/check")
public class VerificationController {

    private final UserService userService;
    private final ProfileService profileService;

    /**
     * 회원가입 시, 사용자 ID 중복 여부를 확인합니다.
     *
     * @param requestBody 요청 데이터 형식: { "userId": "확인할 사용자 ID" }
     * @return 응답 데이터 형식: { "exists": true/false } - true: 이미 존재하는 사용자 ID, false: 사용 가능
     */
    @PostMapping("/userId")
    public ResponseEntity<Map<String, Boolean>> checkUserId(@RequestBody Map<String, String> requestBody) {

        String userId = requestBody.get("userId");
        boolean exists = userService.isUserIdExists(userId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

    /**
     * 회원가입 시, 동일 사용자 이름과 연락처 조합으로 생성할 수 있는 계정은 최대 3개로 제한됩니다.
     *
     * @param requestBody 요청 데이터 형식:
     *                    { "userName": "확인할 사용자 이름",
     *                      "contact": "확인할 사용자 연락처" }
     * @return 응답 데이터 형식: { "userLimit": true/false } - true: 계정 생성 가능, false: 계정 생성 불가
     */
    @PostMapping("/userLimit")
    public ResponseEntity<Map<String, Boolean>> checkUserLimit(@RequestBody Map<String, String> requestBody) {

        String userName = requestBody.get("userName");
        String contact = requestBody.get("contact");

        long userLimit = userService.countUserIds(userName, contact);

        Map<String, Boolean> response = new HashMap<>();
        response.put("userLimit", userLimit < 3);

        return ResponseEntity.ok(response);
    }

    /**
     * 비밀번호 찾기 시, 사용자 이름과 ID를 기반으로 계정 존재 여부를 확인합니다.
     *
     * @param requestBody 요청 데이터 형식:
     *                    { "userName": "확인할 사용자 이름",
     *                      "userId": "확인할 사용자 ID" }
     * @return 응답 데이터 형식: { "exists": true/false } - true: 해당 계정이 존재, false: 해당 계정이 없음
     */
    @PostMapping("/userInfo")
    public ResponseEntity<Map<String, Boolean>> checkUser(@RequestBody Map<String, String> requestBody) {

        String userName = requestBody.get("userName");
        String userId = requestBody.get("userId");

        boolean exists = userService.existsByUserIdAndUserName(userName, userId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

    /**
     * 회원정보 수정 시, 입력된 기존 비밀번호가 저장된 비밀번호와 일치하는지 확인합니다.
     *
     * @param requestBody 요청 데이터 형식:
     *                    { "userId": "확인할 사용자 ID",
     *                      "pw": "입력된 기존 비밀번호" }
     * @return 응답 데이터 형식: { "valid": true/false }
     *         - true: 비밀번호가 일치함
     *         - false: 비밀번호가 일치하지 않음
     */
    @PostMapping("/password")
    public ResponseEntity<Map<String, Boolean>> checkPassword(@RequestBody Map<String, String> requestBody) {

        String userId = requestBody.get("userId");
        String inputPassword = requestBody.get("pw");
        boolean valid = profileService.checkPassword(userId, inputPassword);

        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", valid);

        return ResponseEntity.ok(response);
    }
}
