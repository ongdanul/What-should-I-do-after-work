package com.elice.boardproject.user.controller;

import com.elice.boardproject.profile.service.ProfileService;
import com.elice.boardproject.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/check")
public class SignUpVerificationController {

    private final UserService userService;
    private final ProfileService profileService;

    public SignUpVerificationController(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @PostMapping("/userId")
    public ResponseEntity<Map<String, Boolean>> checkUserId(@RequestBody Map<String, String> requestBody) {

        String userId = requestBody.get("userId");
        boolean exists = userService.isUserIdExists(userId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

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
