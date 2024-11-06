package com.elice.boardproject.post.controller;

import com.elice.boardproject.post.entity.Post;
import com.elice.boardproject.post.entity.PostDto;
import com.elice.boardproject.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/post")
@Controller
public class PostController {
    @Autowired
    private PostService postService;

    // 게시글 전체 목록
    @GetMapping("/list")
    public String lists(String filter, String description, Model model) {
        List<Post> posts = postService.findAll(filter, description);
        model.addAttribute("posts", posts);
        return "post/list";
    }

    // 게시글 상세 페이지
    @GetMapping("/{postId}")
    public String list(@PathVariable Long postId, Model model) {
        Post findPost = postService.detail(postId);

        model.addAttribute("post", findPost);
        return "post/detail";
    }

    // 게시글 등록
    @PostMapping
    public String register(PostDto postDto) {
        Post post = postDto.toPost();
        int newPost = postService.insert(post);

        return "redirect:/post/list";
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    public String update(@PathVariable Long postId, PostDto postDto) {
        Post post = postDto.toPost();
        post.setPostId(postId);
        int updatePost = postService.update(post);

        return "redirect:/post/list/" + postId;
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public String delete(@PathVariable Long postId) {
        postService.delete(postId);
        return "redirect:/post/list";
    }
}
