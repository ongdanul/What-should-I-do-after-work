package com.elice.boardproject.activity.controller;

import com.elice.boardproject.post.entity.PostDto;
import com.elice.boardproject.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    // 작성한 게시물 조회
    @GetMapping
    public String getPosts(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int pageSize, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        List<PostDto> posts = activityService.findPostsByUserId(userId, page, pageSize);
        model.addAttribute("posts", posts);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("activeMenu", "posts");

        return "activity/posts";
    }

    // 즐겨찾기한 게시물 조회
    @GetMapping("/bookmark")
    public String getScraps(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int pageSize, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        List<PostDto> scraps = activityService.findScrapsByUserId(userId, page, pageSize);
        model.addAttribute("scraps", scraps);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("activeMenu", "scraps");

        return "activity/bookmarks";
    }
}
