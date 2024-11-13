package com.elice.boardproject.scrap.controller;

import com.elice.boardproject.scrap.entity.ScrapDto;
import com.elice.boardproject.scrap.service.ScrapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/bookmark")
@RestController
public class ScrapController {
    @Autowired
    private ScrapService scrapService;

    // 즐겨찾기 상태
    @GetMapping("/{postId}")
    public ResponseEntity<ScrapDto> scrapStatus(@PathVariable("postId") Long postId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        ScrapDto findScrap = new ScrapDto();
        findScrap.setPostId(postId);
        findScrap.setUserId(userId);
        findScrap.setExist(scrapService.isScrap(userId, postId));

        return new ResponseEntity<>(findScrap, HttpStatus.OK);
    }

    // 즐겨찾기 토글
    @PostMapping("/{postId}")
    public ResponseEntity<Integer> toggleScrap(@PathVariable("postId") Long postId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        boolean isScrap = scrapService.isScrap(userId, postId);

        if (isScrap) {
            scrapService.delete(userId, postId);
            return new ResponseEntity<>(0, HttpStatus.NO_CONTENT);
        } else {
            scrapService.insert(userId, postId);
            return new ResponseEntity<>(1, HttpStatus.CREATED);
        }
    }
}
