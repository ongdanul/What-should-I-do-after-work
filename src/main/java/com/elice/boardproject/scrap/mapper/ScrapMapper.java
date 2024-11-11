package com.elice.boardproject.scrap.mapper;

import com.elice.boardproject.scrap.entity.ScrapDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ScrapMapper {
    // 전체 즐겨찾기 목록 조회
    List<ScrapDto> findAll(String userId);

    // 단건 조회
    Optional<ScrapDto> detail(Long ScrapId);

    // 추가
    int insert(ScrapDto scrap);

    // 삭제
    int delete(ScrapDto scrap);

}
