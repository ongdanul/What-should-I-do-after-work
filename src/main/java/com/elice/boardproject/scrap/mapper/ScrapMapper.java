package com.elice.boardproject.scrap.mapper;

import com.elice.boardproject.scrap.entity.ScrapDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ScrapMapper {

    // 즐겨찾기 유무 확인
    Optional<ScrapDto> detail(String userId, Long postId);

    // 추가
    int insert(String userId, Long postId);

    // 삭제
    int delete(String userId, Long postId);

}
