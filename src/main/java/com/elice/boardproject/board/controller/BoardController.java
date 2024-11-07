package com.elice.boardproject.board.controller;

import com.elice.boardproject.board.entity.Board;
import com.elice.boardproject.board.entity.BoardDto;
import com.elice.boardproject.board.service.BoardService;

import com.elice.boardproject.post.entity.Post;
import com.elice.boardproject.post.entity.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/board")
@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    // 게시판 전체 목록
    @GetMapping("/list")
    public String lists(Model model) {
        List<Board> list = boardService.findAll();
        model.addAttribute("board", list);
        return "board/list";
    }

    // 게시판 등록 화면 호출
    @GetMapping("/register")
    public String registerForm(@PathVariable Long boardId, Model model) {
        Board findBoard = boardService.detail(boardId);
        model.addAttribute("board", findBoard);
        return "board/register";
    }

    // 게시판 등록
    @PostMapping("/register")
    public String register(Board board) {
        boardService.insert(board);

        return "redirect:/board/list";
    }

     // 게시판 수정 화면 호출
    @GetMapping("/edit/{boardId}")
    public String updateForm(@PathVariable Long boardId, Model model) {
        System.out.println(boardId);
        Board findBoard = boardService.detail(boardId);
        model.addAttribute("board", findBoard);
        System.out.println(findBoard);
        return "board/edit";
    }

     // 게시판 수정
    @PatchMapping("/{boardId}")
    public String update(@ModelAttribute Board board) {
        System.out.println(board.toString());
        boardService.update(board);
        return "redirect:/board/list";
    }

    // 게시판 삭제
    @DeleteMapping("/{boardId}")
    public String delete(@PathVariable Long boardId) {
        boardService.delete(boardId);
        return "redirect:/board/list";
    }
}


