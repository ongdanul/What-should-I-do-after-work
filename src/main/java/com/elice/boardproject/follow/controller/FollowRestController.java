package com.elice.boardproject.follow.controller;

import com.elice.boardproject.follow.entity.Follow;
import com.elice.boardproject.follow.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class FollowRestController {
    private final FollowService followService;

    public FollowRestController(FollowService followService) {
        this.followService = followService;
    }

    @GetMapping("/follow/{userId}")
    public ResponseEntity<Integer> getCheckFollow(@PathVariable String userId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String myId;
        String newUserId = userId.replace("\"", "");
        if (principal instanceof UserDetails) {
            myId = ((UserDetails) principal).getUsername();
        } else {
            myId = principal.toString();
        }

        int checkExists = followService.checkFollow(myId, newUserId);

        return new ResponseEntity<>(checkExists, HttpStatus.OK);
    }

    @PostMapping("/follow")
    public ResponseEntity<Integer> following(@RequestBody Follow follow) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String myId;
        String followId = follow.getFollow();
        if (principal instanceof UserDetails) {
            myId = ((UserDetails) principal).getUsername();
        } else {
            myId = principal.toString();
        }

        int checkExists = followService.checkFollow(myId, follow.getFollow());

        int resultInt = -1;

        if (checkExists == 1) {
            followService.delete(myId, followId);
            resultInt = 0;
        } else {
            followService.insert(myId, followId);
            resultInt = 1;
        }

        return new ResponseEntity<>(resultInt, HttpStatus.CREATED);
    }

}
