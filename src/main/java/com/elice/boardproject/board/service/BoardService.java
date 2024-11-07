package com.elice.boardproject.board.service;

import com.elice.boardproject.board.mapper.BoardMapper;
import com.elice.boardproject.board.entity.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardMapper boardMapper;

    public List<Board> findAll() {
        return  boardMapper.findAll();
    }
}
