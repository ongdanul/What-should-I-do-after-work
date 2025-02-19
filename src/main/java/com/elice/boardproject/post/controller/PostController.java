package com.elice.boardproject.post.controller;

import com.elice.boardproject.board.service.BoardService;
import com.elice.boardproject.post.entity.PostDto;
import com.elice.boardproject.post.entity.PostRequestDto;
import com.elice.boardproject.post.service.PostService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/post")
@Controller
public class PostController {
    @Autowired
    private PostService postService;

    private final BoardService boardService;

    public PostController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시글 전체 목록
    @GetMapping("/list/{boardId}")
    public String lists(@PathVariable Long boardId, @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "reg_date") String orderBy, Model model) {
        List<PostDto> posts = postService.findAll(boardId, page, pageSize, orderBy);
        String boardTitle = boardService.findBoardTitle(boardId);
        model.addAttribute("posts", posts);
        model.addAttribute("boardId", boardId);
        model.addAttribute("boardTitle", boardTitle);
        model.addAttribute("page", page);
        model.addAttribute("orderBy", orderBy);

        return "post/list";
    }

    // 게시글 필터 목록
    @GetMapping("/list")
    public String postFilter(String filter, String description, Long boardId, @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int pageSize, Model model) {
        List<PostDto> posts = postService.postFilter(filter, description, boardId, page, pageSize);
        model.addAttribute("posts", posts);
        model.addAttribute("boardId", boardId);
        model.addAttribute("page", page);

        return "post/list";
    }

    // 게시글 상세 페이지
    @GetMapping("/{postId}")
    public String list(@PathVariable Long postId, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String authenticatedUserId;

        if (principal instanceof UserDetails) {
            authenticatedUserId = ((UserDetails) principal).getUsername();
        } else {
            authenticatedUserId = principal.toString();
        }
        PostDto findPost = postService.detail(postId);
        model.addAttribute("post", findPost);
        model.addAttribute("authenticatedUserId", authenticatedUserId);

        return "post/detail";
    }

    // 게시글 등록 화면 호출
    @GetMapping("/register")
    public String registerForm(@RequestParam Long boardId, Model model) {
        model.addAttribute("boardId", boardId);
        model.addAttribute("post", new PostDto());

        return "post/register";
    }

    // 게시글 등록
    @PostMapping("/register")
    public String register(@RequestParam Long boardId, PostRequestDto postRequestDto, HttpSession httpSession) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        PostDto postDto = postRequestDto.toPostDto();
        postDto.setBoardId(boardId);
        postDto.setUserId(userId);

        postService.insert(postDto);

        return "redirect:/post/list/" + postDto.getBoardId();
    }

    // 게시글 수정 화면 호출
    @GetMapping("/edit/{postId}")
    public String updateForm(@PathVariable Long postId, Model model) {
        PostDto findPost = postService.detail(postId);
        model.addAttribute("post", findPost);

        return "post/edit";
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    public String update(@PathVariable Long postId, @ModelAttribute PostRequestDto postRequestDto) {
        PostDto post = postRequestDto.toPostDto();
        post.setPostId(postId);
        postService.update(post);

        return "redirect:/post/" + postId;
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public String delete(@PathVariable Long postId) {
        PostDto post = postService.detail(postId);
        postService.delete(postId);

        return "redirect:/post/list/" + post.getBoardId();
    }
}
