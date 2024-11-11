package com.elice.boardproject.board.service;

import com.elice.boardproject.board.entity.BoardDto;
import com.elice.boardproject.board.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class BoardService {
    @Autowired
    private BoardMapper boardMapper;

    // 게시판 전체 목록 조회
    public List<BoardDto> findAll() {
        log.info("[findAll 호출]");
        return  boardMapper.findAll();
    }

    //게시판 단건 조회
    public BoardDto detail(Long boardId) {
        log.info("[detail 호출]  게시판 아이디 : {}", boardId);
        return boardMapper.detail(boardId)
                .orElseThrow(() -> {
                    log.warn("게시판 아이디 : {}을 찾을 수 없습니다.", boardId);
                    return new RuntimeException("게시판을 찾을 수 없습니다.");
                });
    }

    // 게시판 등록
    public void insert(BoardDto board) {
        log.info("[insert 호출] 게시판 등록 : {}", board);

        int exists = boardMapper.insert(board);

        if (exists == 0) {
            log.warn("게시판 등록 실패 : {}", board);
            throw new RuntimeException("게시판 등록에 실패했습니다.");
        }

        log.info("게시판 등록 성공 : {}", board);
    }

    // 게시판 수정
    @Transactional
    public void update(BoardDto board) {
        log.info("[update 호출] 게시판 수정 : {}", board);

        BoardDto findBoard = boardMapper.detail(board.getBoardId())
                .orElseThrow(() -> {
                    log.warn("게시판 아이디 : {}을 찾을 수 없습니다.", board.getBoardId());
                    throw new RuntimeException("게시판을 찾을 수 없습니다");
                });

        int exists = boardMapper.update(board);

        if (exists == 0) {
            log.warn("게시판 수정 실패 : {}", board);
            throw new RuntimeException("게시판 수정에 실패했습니다.");
        }

        log.info("게시판 수정 성공 : {}", board);
    }

    // 게시판 삭제
    public void delete(Long boardId) {
        log.info("[delete 호출]  게시판 아이디 : {}", boardId);

        BoardDto findBoard = boardMapper.detail(boardId)
                .orElseThrow(() -> {
                    log.warn("게시판 아이디 : {}을 찾을 수 없습니다.", boardId);
                    throw new RuntimeException("게시판을 찾을 수 없습니다");
                });

        int exists = boardMapper.delete(findBoard);

        if (exists == 0) {
            log.warn("게시판 삭제 실패 : {}", boardId);
            throw new RuntimeException("게시판 삭제에 실패했습니다.");
        }

        log.info("게시판 삭제 성공 : {}", boardId);
    }
}
