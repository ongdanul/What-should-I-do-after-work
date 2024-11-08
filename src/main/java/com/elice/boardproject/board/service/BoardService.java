package com.elice.boardproject.board.service;

import com.elice.boardproject.board.entity.BoardDto;
import com.elice.boardproject.board.entity.BoardResponseDto;
import com.elice.boardproject.board.mapper.BoardMapper;
import com.elice.boardproject.board.entity.Board;
import com.elice.boardproject.post.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardMapper boardMapper;

    // 게시판 전체 목록 조회
    public List<BoardDto> findAll() {
        return  boardMapper.findAll();
    }

    //게시판 단건 조회
    public BoardDto detail(Long boardId) {
        BoardDto findBoard = boardMapper.detail(boardId);

        if (findBoard == null) {
            throw new RuntimeException();
        }

        return findBoard;
    }

    // 게시판 등록
    public int insert(BoardDto board) {
        int exists = boardMapper.insert(board);

        if (exists == 0) {
            throw new RuntimeException();
        }

        return exists;
    }

    // 게시판 수정
    @Transactional
    public int update(BoardDto board) {
        int exists = boardMapper.update(board);

        if (exists == 0) {
            throw new RuntimeException();
        }

        return exists;
    }

    // 게시판 삭제
    public void delete(Long boardId) {
        BoardDto findBoard = boardMapper.detail(boardId);

        if (findBoard == null) {
            throw new RuntimeException();
        }

        boardMapper.delete(findBoard);
    }
}
