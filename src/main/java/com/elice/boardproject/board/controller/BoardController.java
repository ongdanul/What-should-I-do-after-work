package com.elice.boardproject.board.controller;

import com.elice.boardproject.board.entity.BoardDto;
import com.elice.boardproject.board.entity.BoardRequestDto;
import com.elice.boardproject.board.service.BoardService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/board")
@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    // 게시판 전체 목록
    @GetMapping("/list")
    public String lists(Model model) {
        List<BoardDto> boardLists = boardService.findAll();

        List<List<BoardDto>> boardChunks = new ArrayList<>();
        for (int i = 0; i < boardLists.size(); i += 10) {
            int end = Math.min(i + 10, boardLists.size());
            boardChunks.add(boardLists.subList(i, end));
        }

        model.addAttribute("boards", boardChunks);

        return "board/list";
    }

    // 게시판 등록 화면 호출
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("board", new BoardDto());

        return "board/register";
    }

    // 게시판 등록
    @PostMapping("/register")
    public String register(@ModelAttribute BoardRequestDto boardRequestDto) {
        BoardDto board = boardRequestDto.toBoardDto();
        boardService.insert(board);

        return "redirect:/board/list";
    }

     // 게시판 수정 화면 호출
    @GetMapping("/edit/{boardId}")
    public String updateForm(@PathVariable Long boardId, Model model) {
        BoardDto findBoard = boardService.detail(boardId);
        model.addAttribute("board", findBoard);

        return "board/edit";
    }

     // 게시판 수정
    @PatchMapping("/{boardId}")
    public String update(@RequestBody BoardRequestDto boardRequestDto, @PathVariable Long boardId) {
        BoardDto board = boardRequestDto.toBoardDto();
        board.setBoardId(boardId);
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


