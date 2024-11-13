package com.elice.boardproject.scrap.controller;

import com.elice.boardproject.scrap.service.ScrapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequestMapping("/bookmark")
@RestController
public class ScrapController {
    @Autowired
    private ScrapService scrapService;

    // 즐겨찾기 등록/삭제
    @PostMapping("/{postId}")
    public ResponseEntity<?> insert(@PathVariable Long postId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        int success = scrapService.insert(userId, postId);

        if (success == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(success, HttpStatus.CREATED);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> delete(@PathVariable Long postId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        scrapService.delete(userId, postId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
