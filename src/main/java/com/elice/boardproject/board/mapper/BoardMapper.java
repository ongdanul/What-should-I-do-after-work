package com.elice.boardproject.board.mapper;

import com.elice.boardproject.board.entity.Board;
import com.elice.boardproject.post.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    // 전체 조회
    List<Board> findAll();

    // 단건 조회
    Board detail(Long boardId);

    // 등록
    int insert(Board board);

    // 수정
    int update(Board board);

    // 삭제
    void delete(Board board);
}
