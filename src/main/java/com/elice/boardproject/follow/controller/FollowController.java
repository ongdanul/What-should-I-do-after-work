package com.elice.boardproject.follow.controller;


import com.elice.boardproject.follow.service.FollowService;
import com.elice.boardproject.post.entity.PostDto;
import com.elice.boardproject.user.entity.Users;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/activity")
@Controller
public class FollowController {
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @GetMapping("/follow")
    public String follow(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        List<PostDto> posts = followService.findFollow(userId);

        model.addAttribute("posts", posts);
        model.addAttribute("activeMenu", "follow");

        return "activity/follow";
    }

    @GetMapping("/follower")
    public String follower(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        List<Users> users = followService.findFollower(userId);

        model.addAttribute("users", users);
        model.addAttribute("activeMenu", "follower");

        return "activity/follower";
    }
}
