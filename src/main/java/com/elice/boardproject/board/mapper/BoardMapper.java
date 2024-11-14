package com.elice.boardproject.board.mapper;

import com.elice.boardproject.board.entity.Board;
import com.elice.boardproject.board.entity.BoardDto;
import com.elice.boardproject.board.entity.BoardResponseDto;
import com.elice.boardproject.post.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    // 전체 조회
    List<BoardDto> findAll();

    // 단건 조회
    Optional<BoardDto> detail(Long boardId);

    // 등록
    int insert(BoardDto board);

    // 수정
    int update(BoardDto board);

    // 삭제
    int delete(BoardDto board);

    String findBoardTitle(Long boardId);
}
