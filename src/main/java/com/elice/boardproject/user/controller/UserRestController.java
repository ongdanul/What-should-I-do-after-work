package com.elice.boardproject.user.controller;

import com.elice.boardproject.user.dto.SignUpDTO;
import com.elice.boardproject.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    /**
     * 사용자 회원가입을 처리하는 API입니다.
     * @param signUpDTO 사용자 정보가 담긴 DTO
     * @param bindingResult 유효성 검사 결과
     * @return 회원가입 성공/실패에 대한 응답을 반환
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Map<String,Object>> signUpProcess(@Valid SignUpDTO signUpDTO, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("success", false);
            response.put("message", "The input values are not valid.");
            return ResponseEntity.badRequest().body(response);
        }

        boolean isSignedUp = userService.signUpProcess(signUpDTO);
        if (isSignedUp) {
            response.put("success", true);
            response.put("message", "Sign-up successful.");
            response.put("userName", signUpDTO.getUserName());
        } else {
            response.put("success", false);
            response.put("message", "Sign-up failed. Please try again.");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 사용자의 아이디를 찾는 API입니다.
     * @param requestBody 사용자명과 연락처를 담은 Map 객체
     * @return 해당하는 아이디 리스트 또는 아이디를 찾을 수 없을 경우 404 응답
     */
    @PostMapping("/find-id")
    public ResponseEntity<List<String>> findId(@RequestBody Map<String, String> requestBody) {

        String userName = requestBody.get("userName");
        String contact = requestBody.get("contact");

        List<String> userIds = userService.findByUserId(userName, contact);

        if (userIds != null && !userIds.isEmpty()) {
            return ResponseEntity.ok(userIds);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    /**
     * 사용자의 비밀번호를 찾는 API입니다.
     * @param requestBody 사용자명과 아이디를 담은 Map 객체
     * @return 비밀번호 찾기 처리 결과에 따른 응답
     */
    @PostMapping("/find-pw")
    public ResponseEntity<String> findPw(@RequestBody Map<String, String> requestBody) {
        String userName = requestBody.get("userName");
        String userId = requestBody.get("userId");

        String resultMessage = userService.findByUserdPw(userName, userId);

        if (resultMessage.equals("Temporary password issuance has been completed.")) {
            return ResponseEntity.ok("임시 비밀번호 발급이 완료되었습니다.");
        } else if (resultMessage.equals("User not found.")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
}