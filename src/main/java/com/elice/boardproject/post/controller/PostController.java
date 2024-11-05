package com.elice.boardproject.post.controller;

import com.elice.boardproject.post.entity.Post;
import com.elice.boardproject.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/post")
@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/list")
    public String Lists(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "post/list";
    }
}
