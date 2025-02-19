package com.elice.boardproject.scrap.service;

import com.elice.boardproject.scrap.entity.ScrapDto;
import com.elice.boardproject.scrap.mapper.ScrapMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ScrapService {
    @Autowired
    private ScrapMapper scrapMapper;

    //즐겨찾기 확인
    public boolean isScrap(String userId, Long postId) {
        Optional<ScrapDto> findScrap = scrapMapper.detail(userId, postId);

        if (findScrap.isPresent()) {
            return true;
        }

        return false;
    }

    // 즐겨찾기 등록
    public void insert(String userId, Long postId) {
        scrapMapper.insert(userId, postId);

    }

    // 즐겨찾기 삭제
    public void delete(String userId, Long postId) {
        scrapMapper.delete(userId, postId);
    }
}
